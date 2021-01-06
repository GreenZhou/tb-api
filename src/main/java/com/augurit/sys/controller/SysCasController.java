package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.CaptchaUtil;
import com.augurit.common.utils.EncodeUtil;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Api(tags ="登录管理")
@RestController
@RequestMapping("/sys/cas")
public class SysCasController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpSession session;

    @Autowired
    private SysUserService sysUserService;

    @Value("${cas.server-url-prefix}")
    public String casServerUrl;

    @ApiOperation(value ="退出登录")
    @ApiImplicitParam(name="redirect",value="退出后重定向的url")
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public void logout(String redirect) throws Exception{
        session.invalidate();//清空cas用户信息
        String redirectUrl=casServerUrl+"/logout?service="+redirect;
        response.sendRedirect(redirectUrl);
    }

    /**
     * 登录接口类
     * 返回200状态码并且格式为{"@class":"org.apereo.cas.authentication.principal.SimplePrincipal","id":"casuser","attributes":{}} 是成功的
     * 返回状态码403用户不可用；404账号不存在；423账户被锁定；428过期；其他登录失败
     */
    //21232f297a57a5a743894a0e4a801fc3
    @AgLog("登录")
    @RequestMapping("/login")
    @ApiOperation(value="登录",hidden = true)
    public Object login(@RequestHeader HttpHeaders httpHeaders) {
        String authorization = httpHeaders.getFirst("authorization");
        CasUser inputCasUser = getUserCredential(authorization);
        String userName = inputCasUser.getUserName();
        String password = inputCasUser.getPassword();
        CasUser dbCasUser = sysUserService.queryCasUserByUserName(userName);

        //用户不存在
        if (dbCasUser == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        //账号不处于账户有效期内
        Date startTime=dbCasUser.getStartTime();
        Date endTime=dbCasUser.getEndTime();
        if(startTime!=null||endTime!=null){
            //获取当前时间
            long nowTime = new Date().getTime();
            //获取开始时间
            if (startTime != null) {
                long start=startTime.getTime();
                if(nowTime<start){
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
            //获取结束时间
            if (endTime != null) {
                long end=endTime.getTime();
                if(nowTime>end){
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
        }

        //用户被锁
        if (dbCasUser.getLocked()!=null && dbCasUser.getLocked() == 0) {
            //用户被锁定,锁定超过30分钟解锁
            if(this.boolDate(dbCasUser.getErrorTime())){
                //解锁
                dbCasUser.setLocked(1);
                dbCasUser.setLockedNum(0);
                dbCasUser.setErrorTime(null);
            }else {
                return new ResponseEntity(HttpStatus.LOCKED);
            }
        }
        if (dbCasUser.getStatus()!=null && dbCasUser.getStatus() == 0) {//用户被禁用
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String encodedPassword = getEncodedPassword(password, dbCasUser);
        String userPassword = dbCasUser.getPassword();
        if (encodedPassword.equals(userPassword)) {//登录成功
            dbCasUser.setLockedNum(0);
            dbCasUser.setLocked(1);
            dbCasUser.setErrorTime(null);
            sysUserService.updateCasUser(dbCasUser.getLocked(),dbCasUser.getLockedNum(),dbCasUser.getErrorTime(),dbCasUser.getUserId());
            return dbCasUser;
        } else {
            int lockedNum = dbCasUser.getLockedNum();
            //密码错误,错误3次锁定帐号30分钟
            if(lockedNum>=3){
                dbCasUser.setLocked(0);
                dbCasUser.setLockedNum(++lockedNum);
                dbCasUser.setErrorTime(new Date());
                sysUserService.updateCasUser(dbCasUser.getLocked(),dbCasUser.getLockedNum(),dbCasUser.getErrorTime(),dbCasUser.getUserId());
                return new ResponseEntity(HttpStatus.LOCKED);
            }else {
                //与最后一次登录错误时间超过30min
                if(this.boolDate(dbCasUser.getErrorTime())) {
                    dbCasUser.setLockedNum(1);
                    dbCasUser.setErrorTime(new Date());
                }else {
                    dbCasUser.setLockedNum(++lockedNum);
                    dbCasUser.setErrorTime(new Date());
                }
                sysUserService.updateCasUser(dbCasUser.getLocked(),dbCasUser.getLockedNum(),dbCasUser.getErrorTime(),dbCasUser.getUserId());
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    /**
     * 获取加密后的密码
     */
    private String getEncodedPassword(String password, CasUser sysUser) {
        String salt = sysUser.getSalt();
        String encodedPassword = EncodeUtil.encode(password, salt);
        return encodedPassword;
    }

    /**
     * 获取用户名密码
     */
    public CasUser getUserCredential(String authorization) {
        String baseCredentials = authorization.split(" ")[1];
        String usernamePassword = null;
        try {
            usernamePassword = new String(Base64Utils.decodeFromString(baseCredentials), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] credentials = usernamePassword.split(":");
//        CasUser casUser=new CasUser(credentials[0],credentials[1],credentials[2]);
        CasUser casUser=new CasUser(credentials[0],credentials[1],"");
        return casUser;
    }

    /**
     * 判断当前时间与最后一次登录错误时间是否超过30min
     */
    private boolean boolDate(Date errorTime){
        if(errorTime==null)
            return true;
        //获取当前时间
        long nowTime = new Date().getTime();
        System.out.println("nowTime:"+nowTime);
        //获取被禁时间
        long banTime = errorTime.getTime();
        System.out.println("banTime:"+banTime);
        //当前时间与最后一次登录错误时间超过30min
        // TODO: 2020/3/5 修改时间
//        return nowTime-banTime>=1800000;
        return nowTime-banTime>=20000;
    }

    @RequestMapping(value="/captcha",method = RequestMethod.GET)
    @ApiOperation(value="获取验证码")
    public void getVerificationCode(HttpServletResponse response, HttpServletRequest request) throws  Exception {
        int width=200;
        int height=70;
        BufferedImage captchaImg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //生成对应宽高的初始图片
        String captcha = CaptchaUtil.drawRandomText(width,height,captchaImg);
        //单独的一个类方法，出于代码复用考虑，进行了封装。
        //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
        request.getSession().setAttribute("captcha", captcha);
        response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
        OutputStream os = response.getOutputStream(); //获取文件输出流
        ImageIO.write(captchaImg,"png",os);//输出图片流
        os.flush();
        os.close();//关闭流
    }

}

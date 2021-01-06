package com.augurit.sys.controller;


import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.service.SysUserRoleService;
import com.augurit.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @ApiOperation(value="获取用户列表")
    @GetMapping("/page")
    public QueryResult<SysUser> page(SysUser sysUser, QueryParam queryParam) {
        PageResult pageResult = sysUserService.page(sysUser, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value="修改用户")
    @AgLog("修改用户")
    @PostMapping("/update")
    public QueryResult update(SysUser user) {
        sysUserService.updateUser(user);
        return QueryResult.success();
    }

    @ApiOperation(value="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "旧密码",name = "oldPsw",required = true),
            @ApiImplicitParam(value = "新密码",name = "newPsw",required = true)
    })
    @AgLog("修改密码")
    @GetMapping("/updatePsw")
    public QueryResult updatePsw(String oldPsw,String newPsw) {
        QueryResult queryResult=sysUserService.updatePsw(oldPsw,newPsw);
        return queryResult;
    }

    @ApiOperation(value="重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户ID数组",name = "userIds",required = true),
            @ApiImplicitParam(value = "新密码",name = "newPsw",required = true)
    })
    @AgLog("重置密码")
    @GetMapping("/resetPsw")
    public QueryResult resetPsw(Long[] userIds,String newPsw) {
        sysUserService.resetPsw(userIds, newPsw);
        return QueryResult.success();
    }

    @ApiOperation(value="删除用户")
    @AgLog("删除用户")
    @GetMapping("/delete")
    public QueryResult delete(@RequestParam("userId") Long[] userId) {
        // TODO: 2020/3/4  不能删除系统管理员，不能删除当前登录用户
        sysUserService.delete(userId);
        return QueryResult.success();
    }

    @ApiOperation(value="新增用户")
    @AgLog("新增用户")
    @PostMapping("/save")
    public QueryResult save(SysUser sysUser) {
        sysUserService.saveUser(sysUser);
        return QueryResult.success();
    }

    @ApiOperation(value="用户移入机构")
    @AgLog("用户移入机构")
    @GetMapping("/moveToDept")
    public QueryResult moveToDept(Long[] userIds,Long[] deptIds) {
        sysUserService.moveToDept(userIds,deptIds);
        return QueryResult.success();
    }

    @ApiOperation(value="获取用户详细信息")
    @GetMapping("/info")
    public QueryResult info(Long userId) {
        SysUser sysUser=sysUserService.info(userId);
        return QueryResult.success().addEntity(sysUser);
    }

    @ApiOperation(value="查询用户是否已注册")
    @ApiImplicitParam(value = "用户名",name = "username",required = true)
    @GetMapping("/infoByName")
    public QueryResult infoByName(String username) {
        SysUser sysUser=sysUserService.infoByName(username);
        return QueryResult.success().addEntity(sysUser);
    }

    @ApiOperation(value = "获取机构下的所有用户")
    @GetMapping(value="/getSubDeptUsers")
    public QueryResult<SysUser> getSubDeptUsers(Long currentDeptId, SysUser sysUser,QueryParam queryParam) {
        PageResult pageResult=sysUserService.getSubDeptUsers(currentDeptId,sysUser,queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    /**
     * 获取当前用户信息
     */
    @ApiOperation(value = "获取当前登录用户")
    @GetMapping("/current")
    public QueryResult current() {
        CasUser casUser =sysUserService.current();
		return QueryResult.success().addEntity(casUser);
    }

    /**
     * 用户信息
     */
//    @RequestMapping("/msg")
//    public ResultBean msg(@RequestParam("userId") Long userId) {
//        SysUser user = sysUserService.getById(userId);
//
//        //获取用户所属的角色列表
//        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
//        user.setRoleIdList(roleIdList);
//
//        return ResultBean.ok().put("data", user);
//    }

    @ApiOperation(value = "更改用户列表的排序")
    @AgLog("更改用户列表的排序")
    @GetMapping("/updateOrderNum")
    public QueryResult updateOrderNum(Long startOrderNum, Long endOrderNum){
        sysUserService.updateOrderNum(startOrderNum,endOrderNum);
        return QueryResult.success();
    }
}

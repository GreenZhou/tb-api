package com.augurit.common.aspect;

import com.alibaba.fastjson.JSON;
import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.HttpContextUtils;
import com.augurit.common.utils.IPUtils;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.entity.SysLog;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.service.SysLogService;
import com.augurit.sys.service.SysUserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private SysUserService sysUserService;

	@Pointcut("@annotation(com.augurit.common.annotation.AgLog)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time,result);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time,Object result) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		AgLog syslog = method.getAnnotation(AgLog.class);
		if(syslog != null){
			//注解上的描述
			sysLog.setOperation(syslog.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = JSON.toJSONString(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username=null;
		if(result instanceof CasUser && result!=null){
			CasUser casUser = (CasUser)result;
			username=casUser.getUserName();
		}else {
			// TODO: 2020/6/1 密码错误时会报错
//			username = AssertionHolder.getAssertion().getPrincipal().getName();
			username="admin";
		}
		SysUser sysUser=sysUserService.getUserByUsername(username);
		sysLog.setUsername(username);
		sysLog.setNickname(sysUser.getNickname());
		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
        if(result != null && result instanceof QueryResult){
            sysLog.setResult(((QueryResult) result).getCode());
        }
		//保存系统日志
		sysLogService.save(sysLog);
	}
}

package com.augurit.common.exception;

import com.augurit.common.utils.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class AGExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(AGException.class)
	public QueryResult handleRRException(AGException e){
		QueryResult resultBean = new QueryResult();
		resultBean.setCode(e.getCode());
		resultBean.setMsg(e.getMessage());
		return resultBean;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public QueryResult handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return QueryResult.error("数据库中已存在该记录");
	}

//	@ExceptionHandler(AuthorizationException.class)
//	public ResultBean handleAuthorizationException(AuthorizationException e){
//		logger.error(e.getMessage(), e);
//		return ResultBean.error("没有权限，请联系管理员授权");
//	}

	@ExceptionHandler(Exception.class)
	public QueryResult handleException(Exception e){
		logger.error(e.getMessage(), e);
		return QueryResult.error();
	}
}

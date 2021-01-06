package com.augurit.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * 请填上日志描述:value
 */
@Target(ElementType.METHOD)// 设置该注解使用的位置
@Retention(RetentionPolicy.RUNTIME)// 设置该注解保留的时间
@Documented
public @interface AgLog {

	String value() default "";
}

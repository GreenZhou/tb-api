package com.augurit.common.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**  表的别名 */
    String tableAlias() default "";

    /**  true：即使转机构后，也能查询本人历史数据 */
    boolean user() default false;

    /**  true：拥有子机构数据权限 */
    boolean subDept() default true;

    /**  机构ID */
    String deptId() default "dept_id";

    /**  用户ID */
    String userId() default "user_id";
}


package com.augurit.common.aspect;


import com.augurit.common.annotation.DataFilter;
import com.augurit.common.exception.AGException;
import com.augurit.common.utils.Constant;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.service.SysDeptService;
import com.augurit.sys.service.SysRoleDeptService;
import com.augurit.sys.service.SysUserRoleService;
import com.augurit.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过滤，切面处理类
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserService sysUserService;

    @Pointcut("@annotation(com.augurit.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if(params != null && params instanceof Map){
            //获取用户
            SysUser user = sysUserService.queryUserIdByUserName(AssertionHolder.getAssertion().getPrincipal().getName());
            //如果不是超级管理员，则进行数据过滤
            if(user.getUserId() != Constant.SUPER_ADMIN){
                Map map = (Map)params;
                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }

            return ;
        }

        throw new AGException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUser user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //先判断有没有表的别名，如果有别名，就在别名后追加.，多表关联查询及过滤数据时，会使用到别名
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        //获取用户的机构，及用户角色对应的机构
        //机构ID列表
        Set<Long> deptIdList = new HashSet<>();
        //用户角色对应的机构ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if(roleIdList.size() > 0){
            Set<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList);
            deptIdList.addAll(userDeptIdList);
        }

        //如果subDept=true，则把用户所在机构的所有子机构查询出来
        //用户子机构ID列表
        if(dataFilter.subDept()){
            // TODO: 2020/5/22 暂时注释
//            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
//            deptIdList.addAll(subDeptIdList);
        }

        //如下代码片段，就是把数据过滤条件组装成dept_id in(1,2,3)
        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if(deptIdList.size() > 0){
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //如下代码片段，就是要实现用户换机构了，不能查看之前机构数据，但还能查看自己的数据
        //没有本机构数据权限，也能查询本人数据
        if(dataFilter.user()){
            if(deptIdList.size() > 0){
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getUserId());
        }

        sqlFilter.append(")");

        if(sqlFilter.toString().trim().equals("()")){
            return null;
        }

        return sqlFilter.toString();
    }
}

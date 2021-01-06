package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class SysRoleMapperSQL {

    /**
     * 获取列表
     */
    public String list(SysRole sysRole){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_role where 1=1");
        if(StringUtils.isNotBlank(sysRole.getRoleName())){
            sb.append(" and role_name like concat('%',#{roleName},'%')");
        }
        sb.append("order by role_id asc");
        return sb.toString();
    }

    /**
     * 获取该角色下的用户id集合
     */
    public String userRoleNum(Long roleId, List<Long> deptIdList, String nickname){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("SELECT DISTINCT u.user_id as userId FROM sys_user_role ur LEFT JOIN sys_user u ON ur.user_id=u.user_id inner join sys_user_dept ud on u.user_id = ud.user_id WHERE 1=1");
        if(roleId!=null){
            sb.append(" and ur.role_id=#{roleId}");
        }
        if(!CollectionUtils.isEmpty(deptIdList)){
            sb.append(" and ud.dept_id in <foreach item='id' collection='deptIdList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        if(StringUtils.isNotBlank(nickname)){
            sb.append(" and u.nickname like concat('%',#{nickname},'%')");
        }
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 获取使用该角色名的数量
     */
    public String getRoleCountByRoleName(String roleName){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(*) FROM sys_role WHERE role_name=#{roleName}");
        return sb.toString();
    }
}

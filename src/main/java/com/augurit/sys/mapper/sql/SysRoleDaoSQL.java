package com.augurit.sys.mapper.sql;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class SysRoleDaoSQL {
    /**
     * 查询子角色ID列表
     */
    public String queryChildRoleIdList(Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("select role_id from sys_role where parent_id = #{parentId}");
        return sb.toString();
    }

    /**
     * 查询父角色ID列表
     */
    public String queryParentRoleIdList(Long childId){
        StringBuilder sb = new StringBuilder();
        sb.append("select parent_id from sys_role where role_id = #{childId}");
        return sb.toString();
    }

    /**
     * 显示该角色下的所有用户
     */
    public String userList(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT user_id FROM sys_user_role WHERE role_id=#{roleId}");
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
}

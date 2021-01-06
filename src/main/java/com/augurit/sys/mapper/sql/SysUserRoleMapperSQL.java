package com.augurit.sys.mapper.sql;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class SysUserRoleMapperSQL {
    public String queryRoleIdList(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("select role_id from sys_user_role where user_id = #{userId}");
        return sb.toString();
    }

    public String queryRoleList(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("select r.* from sys_user_role ur left join sys_role r on ur.role_id = r.role_id where ur.user_id = #{userId} order by r.role_id asc");
        return sb.toString();
    }

    /**
     * 根据角色ID数组，批量删除
     */
    public String deleteByUserId(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_user_role WHERE user_id = #{userId}");
        return sb.toString();
    }

    /**
     * 根据角色ID数组，批量删除
     */
    public String deleteByRoleId(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_user_role WHERE role_id =#{roleId}");
        return sb.toString();
    }

    /**
     * 根据角色ID和用户ID数组，批量删除
     */
    public String deleteByRoleIdUserIds(Long roleId, Long[] userIds){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_user_role WHERE role_id = #{roleId}");
        sb.append(" and user_id in <foreach item='item' collection='userIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }

    public String queryUserRole(Long roleId, String nickname){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT u.user_id,u.nickname,u.username");
        sb.append(",(select string_agg(ud.dept_id||'' , ',' order by ud.id asc) from sys_user_dept ud where ud.user_id = u.user_id) as deptIds" );
        sb.append(" FROM sys_user_role ur LEFT JOIN sys_user u on ur.user_id=u.user_id WHERE 1=1");
        if(roleId!=null){
            sb.append(" and ur.role_id=#{roleId}");
        }
        if(StringUtils.isNotBlank(nickname)){
            sb.append(" and u.nickname like concat('%',#{nickname},'%')");
        }
        return sb.toString();
    }

    /**
     * 根据用户id列表或角色id列表删除
     */
    public String deleteBatch(List<Long> userList, List<Long> roleList){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_user_role WHERE 1=1");
        if(!CollectionUtils.isEmpty(roleList)){
            sb.append(" and role_id in <foreach item='id' collection='roleList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        if(!CollectionUtils.isEmpty(userList)){
            sb.append(" and user_id in <foreach item='id' collection='userList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        sb.append("</script>");
        return sb.toString();
    }
}

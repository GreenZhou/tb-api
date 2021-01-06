package com.augurit.sys.mapper.sql;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public class SysUserDeptMapperSQL {
    /**
     * 根据角色ID，批量删除
     */
    public String deleteByUserId(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_user_dept WHERE user_id = #{userId}");
        return sb.toString();
    }

    /**
     * 根据角色ID数组，批量删除
     */
    public String deleteByUserIds(@Param("userIds") Long[] userIds){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_user_dept WHERE user_id in <foreach item='item' collection='userIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 移除机构下的用户
     */
    public String deleteByUserIdDeptId(@Param("userIds") Long[] userIds,@Param("subDeptIdList") List<Long> subDeptIdList){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_user_dept WHERE");
        sb.append(" dept_id in <foreach item='item' collection='subDeptIdList' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append(" and user_id in <foreach item='item' collection='userIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }
}

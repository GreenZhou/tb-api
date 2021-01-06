package com.augurit.sys.mapper.sql;

import org.apache.ibatis.annotations.Param;

public class SysRoleFunctionMapperSQL {
    public String getByRoleId(@Param("roleId") Long roleId){
        StringBuilder sb = new StringBuilder("SELECT * from sys_role_function where role_id=#{roleId}");
        return sb.toString();
    }

    public String deleteByRoleFunctionIds(@Param("roleId")Long roleId, @Param("functionIds")Long[] functionIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_role_function");
        sb.append(" WHERE role_id = #{roleId}");
        sb.append(" and function_id in <foreach item='item' collection='functionIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }
}

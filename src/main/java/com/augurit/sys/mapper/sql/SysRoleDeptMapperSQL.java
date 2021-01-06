package com.augurit.sys.mapper.sql;


import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class SysRoleDeptMapperSQL {

    /**
     * 根据角色ID，获取机构ID
     */
    public String queryDeptIdByRoleId(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("select dept_id from sys_role_dept where role_id =#{roleId}");
        return sb.toString();
    }

    /**
     * 根据角色ID，获取机构ID列表
     */
    public String queryDeptIdList(List<Long> list){
//        List<Long> list = new ArrayList<>(Arrays.asList(roleIds));
        StringBuilder sb = new StringBuilder();
        sb.append("select dept_id from sys_role_dept where role_id in(");
        for (int i = 0; i < list.size(); i++) {
            sb.append("'").append(list.get(i)).append("'");
            if (i < list.size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 根据角色ID删除
     */
    public String deleteByRoleId(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_role_dept WHERE role_id = #{roleId}");
        return sb.toString();
    }

    /**
     * 根据角色id列表或部门id列表删除
     */
    public String deleteBatch(List<Long> roleList, List<Long> deptList){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_role_dept WHERE 1=1");
        if(!CollectionUtils.isEmpty(roleList)){
            sb.append(" and role_id in <foreach item='id' collection='roleList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        if(!CollectionUtils.isEmpty(deptList)){
            sb.append(" and dept_id in <foreach item='id' collection='deptList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        sb.append("</script>");
        return sb.toString();
    }
}

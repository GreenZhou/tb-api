package com.augurit.sys.mapper.sql;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class SysRoleMenuMapperSQL {
    public String queryMenuIds(Long roleId){
        StringBuilder sb = new StringBuilder("SELECT menu_id from sys_role_menu where role_id=#{roleId}");
        return sb.toString();
    }

    /**
     * 根据角色ID删除
     */
    public String deleteByRoleId(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_role_menu WHERE role_id = #{roleId}");
        return sb.toString();
    }

    /**
     * 根据菜单ID删除
     */
    public String deleteByMenuId(Long menuId){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_role_menu WHERE menu_id = #{menuId}");
        return sb.toString();
    }

    /**
     * 根据菜单id批量删除
     */
    public String deleteByMenuIdList(List<Long> list){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_role_menu WHERE menu_id IN (");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 根据角色id列表或菜单id列表删除
     */
    public String deleteBatch(List<Long> roleList, List<Long> menuList){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("DELETE FROM sys_role_menu WHERE 1=1 ");
        if(!CollectionUtils.isEmpty(roleList)){
            sb.append(" and role_id in <foreach item='id' collection='roleList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        if(!CollectionUtils.isEmpty(menuList)){
            sb.append(" and menu_id in <foreach item='id' collection='menuList' open='(' separator=',' close=')'>#{id}</foreach>");
        }
        sb.append("</script>");
        return sb.toString();
    }
}

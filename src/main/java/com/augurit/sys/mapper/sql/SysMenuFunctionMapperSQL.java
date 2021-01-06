package com.augurit.sys.mapper.sql;

import java.util.List;

import com.augurit.sys.entity.SysMenuFunction;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Param;

public class SysMenuFunctionMapperSQL {
	
    public String queryFuncByMenuId(Long menuId){
        StringBuilder sb = new StringBuilder("SELECT mf.*,f.status,f.name,f.unique_code,f.remark from sys_function f "
        		+ "inner join sys_menu_function mf on mf.function_id = f.function_id where mf.menu_id=#{menuId} order by mf.id asc");
        return sb.toString();
    }

    public String queryOpenFuncByMenuId(Long roleId, Long menuId){
        StringBuilder sb = new StringBuilder("SELECT mf.*,f.status,f.name,f.unique_code,f.remark from sys_function f "
                + "inner join sys_menu_function mf on mf.function_id = f.function_id where mf.menu_id=#{menuId} and f.status = 1 ");
        sb.append(" order by mf.id asc");
        return sb.toString();
    }

    public String updateMenuFuncStatus(Long[] ids,Integer mfStatus){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("UPDATE sys_menu_function SET mf_status=#{mfStatus} ");
        sb.append("WHERE id in <foreach item='item' collection='ids' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 根据菜单id批量删除
     */
    public String deleteByMenuIdList(List<Long> list){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM sys_menu_function WHERE menu_id IN (");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 根据菜单id 和功能编码，获取功能列表
     */
    public String queryFuncByCode(Long menuId, String uniqueCode){
        StringBuilder sb = new StringBuilder("SELECT f.* from sys_function f inner join sys_menu_function mf on" +
                " mf.function_id = f.function_id where mf.menu_id = #{menuId} and f.unique_code = #{uniqueCode}");
        return sb.toString();
    }
}

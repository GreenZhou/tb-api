package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class SysMenuMapperSQL {
    public String list(SysMenu sysMenu){
        StringBuilder sb = new StringBuilder();
        sb.append("select t.* ");
        sb.append(",(select string_agg(a.role_id||'' , ',') from sys_role_menu a where a.menu_id = t.menu_id) as roles " );
        sb.append(",(select string_agg(a.function_id||'' , ',' order by a.function_id asc) from sys_menu_function a where a.menu_id = t.menu_id) as funcIds ");
        sb.append(",(select string_agg(f.name||'' , ',' order by mf.function_id asc) from sys_function f inner join sys_menu_function mf" +
                " on mf.function_id = f.function_id where mf.menu_id = t.menu_id) as funcNames ");
        sb.append(" from sys_menu t where 1=1");

        if(sysMenu.getName()!=null && !"".equals(sysMenu.getName())){
            sb.append(" and t.name like concat('%',#{name},'%')");
        }
        if(sysMenu.getUniqueCode()!=null && !"".equals(sysMenu.getUniqueCode())){
            sb.append(" and t.unique_code like concat('%',#{uniqueCode},'%')");
        }
        sb.append(" order by t.menu_id asc");
        return sb.toString();
    }

    public String queryByCode(String uniqueCode){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_menu where unique_code=#{uniqueCode}");
        return sb.toString();
    }
    
    /**
     * 根据菜单ID修改排序
     */
    public String updateByMenuId(Long menuId,int orderNum){
        StringBuilder sb = new StringBuilder();
        sb.append("update sys_menu set order_num = #{orderNum} WHERE menu_id = #{menuId}");
        return sb.toString();
    }
    
    /**
     * 修改菜单状态
     */
    public String updateStatus(Long[] menuIds, Integer status){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("UPDATE sys_menu SET status=#{status} ");
        sb.append("WHERE menu_id in <foreach item='item' collection='menuIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }
    
    public String queryMenusByRole(Long roleId){
        StringBuilder sb = new StringBuilder();
        sb.append("select name,t.menu_id,unique_code from sys_role_menu a ");
        sb.append("left join sys_menu t on a.menu_id = t.menu_id ");
        sb.append("where a.role_id=#{roleId}");
        return sb.toString();
    }

    public String queryMenusByRoleIds(@Param("roleList") List<Long> roleList){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append(" select DISTINCT(m.*) ");
        sb.append(",(select string_agg(f.unique_code||'' , ',' order by mf.function_id asc) from sys_function f inner join sys_menu_function mf" +
                " on mf.function_id = f.function_id inner join sys_role_function rf on rf.function_id = f.function_id where mf.menu_id = m.menu_id and (mf.mf_status = 1 or f.status = 1)) as funcUniqueCodes ");
        sb.append(" from sys_menu m ");
        sb.append(" left join sys_role_menu rm on m.menu_id = rm.menu_id ");
        if(roleList.size()>0) {
            sb.append(" where rm.role_id in <foreach item='item' collection='roleList' open='(' separator=',' close=')'>#{item}</foreach>");
        }else{
            sb.append(" where rm.role_id = -1");
        }
        sb.append("</script>");
        return sb.toString();
    }

    public String queryByObj(SysMenu sysMenu){
        StringBuilder sb = new StringBuilder();
        sb.append("select menu_id, name, parent_id, url, perms, type, icon, order_num from sys_menu where type=#{type} and parent_id=#{parentId}");
        return sb.toString();
    }

    public String queryListParentId(Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("select menu_id, name, parent_id, url, perms, type, icon, order_num from sys_menu where parent_id = #{parentId} order by order_num asc");
        return sb.toString();
    }

    public String queryNotButtonList(){
        StringBuilder sb = new StringBuilder();
        sb.append("select menu_id, name, parent_id, url, perms, type, icon, order_num from sys_menu where type != 2 order by order_num asc");
        return sb.toString();
    }

    public String queryMenuIdListByParentId(Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("select menu_id from sys_menu where parent_id = #{parentId}");
        return sb.toString();
    }

    public String queryRoleIdListByParentId(Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("select menu_id from sys_menu where parent_id = #{parentId}");
        return sb.toString();
    }

}

package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysDept;
import org.apache.ibatis.annotations.Param;

public class SysDeptMapperSQL {

    public String list(SysDept sysDept){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_dept where 1=1");
        if(sysDept.getParentId()!=null){
            sb.append(" and parent_id = #{parentId}");
        }
        sb.append(" order by order_num asc");
        return sb.toString();
    }

    /**
     * 根据角色获取用户列表
     */
//    public String userPage(Long deptId, String nickname){
//        StringBuilder sb = new StringBuilder();
//        sb.append("<script>");
//        sb.append("SELECT sysdept.dept_id as deptId,sysuser.user_id as userId,sysuser.name as username,sysuser.nickname,sysuser.job,sysuser.mobile FROM sys_dept sysdept LEFT JOIN sys_user sysuser ON sysdept.dept_id=sysuser.dept_id WHERE 1=1");
//        if(deptId!=null){
//            sb.append(" and sysdept.dept_id=#{deptId}");
//        }
//        if(StringUtils.isNotBlank(nickname)){
//            sb.append(" and sysuser.nickname like concat('%',#{nickname},'%')");
//        }
//        sb.append("</script>");
//        return sb.toString();
//    }

    /**
     * 根据机构ID修改排序
     */
    public String updateOrderNum(@Param("deptId") Long deptId,@Param("orderNum") int orderNum){
        StringBuilder sb = new StringBuilder();
        sb.append("update sys_dept set order_num = #{orderNum} WHERE dept_id = #{deptId}");
        return sb.toString();
    }

    /**
     * 修改机构的上级机构
     */
    public String updateParentId(@Param("deptId") Long deptId,@Param("parentId") Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("update sys_dept set parent_id = #{parentId} WHERE dept_id = #{deptId}");
        return sb.toString();
    }
}

package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public class SysUserMapperSQL {

    /**
     * 获取用户列表
     */
    public String list(SysUser sysUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("select u.*");
        sb.append(",(select string_agg(sud.dept_id||'' , ',' order by sud.id asc) from sys_user_dept sud where sud.user_id = u.user_id) as deptIds" );
        sb.append(" from sys_user u left join sys_user_dept ud on ud.user_id = u.user_id");
        sb.append(" where 1=1 ");
        if (StringUtils.isNotBlank(sysUser.getUsername())) {
            sb.append(" and u.username like concat('%',#{username},'%')");
        }
        if (StringUtils.isNotBlank(sysUser.getNickname())) {
            sb.append(" and u.nickname like concat('%',#{nickname},'%')");
        }
        if (StringUtils.isNotBlank(sysUser.getRemark())) {
            sb.append(" and u.remark like concat('%',#{remark},'%')");
        }
        if(!CollectionUtils.isEmpty(sysUser.getSubDeptIdList())){
            sb.append(" and ud.dept_id in <foreach item='item' collection='subDeptIdList' open='(' separator=',' close=')'>#{item}</foreach>");
        }
        sb.append(" ORDER BY user_id asc");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 根据用户名获取用户id,机构id
     */
    public String queryUserIdByUserName(String userName){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT u.user_id");
        sb.append(",(select string_agg(sud.dept_id||'' , ',' order by sud.id asc) from sys_user_dept sud where sud.user_id = u.user_id) as deptIds" );
        sb.append(" FROM sys_user u WHERE username=#{userName}");
        return sb.toString();
    }

    /**
     * 根据用户名获取用户登录信息
     */
    public String queryCasUserByUserName(String username){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sys_user WHERE username=#{userName}");
        return sb.toString();
    }

    /**
     * 修改cas
     */
    public String updateCasUser(int locked, int lockedNum, Date errorTime,Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE sys_user SET locked=#{locked},locked_num=#{lockedNum},error_time=#{errorTime} WHERE user_id=#{userId}");
        return sb.toString();
    }

    /**
     * 根据机构ID获取用户
     */
    public String getUsersByDeptIds(@Param("subDeptIds") Long[] subDeptIds,@Param("sysUser") SysUser sysUser){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("select distinct u.*");
        sb.append(",(select string_agg(sud.dept_id||'' , ',' order by sd.dept_id asc) from sys_user_dept sud left join sys_dept sd on sud.dept_id = sd.dept_id where sud.user_id = u.user_id) as deptIds" );
        sb.append(",(select string_agg(sd.name||'' , ',' order by sd.dept_id asc) from sys_user_dept sud left join sys_dept sd on sud.dept_id = sd.dept_id where sud.user_id = u.user_id) as deptNames" );
        sb.append(" from sys_user u left join sys_user_dept ud on u.user_id = ud.user_id left join sys_dept d on ud.dept_id = d.dept_id  where 1=1 ");
        if(subDeptIds.length>0) {
            sb.append(" and ud.dept_id in <foreach item='item' collection='subDeptIds' open='(' separator=',' close=')'>#{item}</foreach>");
        }
        if(sysUser.getNickname()!=null){
            sb.append(" and nickname like concat('%',#{sysUser.nickname},'%')");
        }
        if(sysUser.getUsername()!=null){
            sb.append(" and username like concat('%',#{sysUser.username},'%')");
        }
        sb.append(" order by u.order_num asc");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 根据机构ID集合获取用户数量
     */
    public String getUsersCountByDeptIds(@Param("deptIds") Long[] deptIds){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("select count(distinct u.*) from sys_user_dept ud left join sys_user u on ud.user_id = u.user_id where 1=1 ");
        sb.append("and ud.dept_id in <foreach item='item' collection='deptIds' open='(' separator=',' close=')'>#{item}</foreach>");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 根据机构ID获取用户数量
     */
    public String getUsersCountByDeptId(Long deptId){
        StringBuilder sb = new StringBuilder();
        sb.append("select count(distinct u.*) from sys_user_dept ud left join sys_user u on ud.user_id = u.user_id where");
        sb.append(" ud.dept_id = #{deptId}");
        return sb.toString();
    }

    /**
     * 根据用户名获取用户
     */
    public String getUserByUsername(String username){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_user where username = #{username}");
        return sb.toString();
    }

    /**
     * 根据ID获取带部门信息的用户
     */
    public String getUserWithDeptById(Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("select distinct u.*");
        sb.append(",(select string_agg(sud.dept_id||'' , ',' order by sd.dept_id asc) from sys_user_dept sud left join sys_dept sd on sud.dept_id = sd.dept_id where sud.user_id = u.user_id) as deptIds" );
        sb.append(",(select string_agg(sd.name||'' , ',' order by sd.dept_id asc) from sys_user_dept sud left join sys_dept sd on sud.dept_id = sd.dept_id where sud.user_id = u.user_id) as deptNames" );
        sb.append(" from sys_user u where user_id = #{userId}");
        sb.append(" order by u.user_id asc");
        sb.append("</script>");
        return sb.toString();
    }

    /**
     * 根据ID修改排序
     */
    public String updateOrderNumById(@Param("orderNum")Long orderNum,@Param("userId")Long userId){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE sys_user SET order_num=#{orderNum} WHERE user_id=#{userId}");
        return sb.toString();
    }

    /**
     * 根据开始和结束排序获取之间的用户
     */
    public String getUsersByStartEndOrder(@Param("startOrderNum")Long startOrderNum,@Param("endOrderNum")Long endOrderNum){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_user where order_num >= #{startOrderNum} and order_num <= #{endOrderNum} order by order_num asc");
        return sb.toString();
    }

}

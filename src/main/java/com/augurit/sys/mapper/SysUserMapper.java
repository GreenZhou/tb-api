package com.augurit.sys.mapper;

import com.augurit.sys.entity.CasUser;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.mapper.sql.SysUserMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Date;
import java.util.List;

/**
 * 机构管理
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser>  {

    @SelectProvider(type = SysUserMapperSQL.class,method ="list")
    List<SysUser> list(SysUser sysUser);

    /**
     *	根据用户名获取用户登录信息
     */
    @SelectProvider(type = SysUserMapperSQL.class,method ="queryCasUserByUserName")
    CasUser queryCasUserByUserName(String userName);

    /**
     *	根据用户名获取用户登录信息
     */
    @SelectProvider(type = SysUserMapperSQL.class,method ="queryUserIdByUserName")
    SysUser queryUserIdByUserName(String userName);

    /**
     *	修改cas
     */
    @UpdateProvider(type = SysUserMapperSQL.class,method ="updateCasUser")
    int updateCasUser(int locked, int lockedNum, Date errorTime, Long userId);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUsersByDeptIds")
    List<SysUser> getUsersByDeptIds(@Param("subDeptIds") Long[] subDeptIds,@Param("sysUser") SysUser sysUser);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUsersCountByDeptIds")
    Long getUsersCountByDeptIds(@Param("deptIds")Long[] deptIds);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUsersCountByDeptId")
    Long getUsersCountByDeptId(Long deptId);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUserByUsername")
    SysUser getUserByUsername(String username);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUserWithDeptById")
    SysUser getUserWithDeptById(Long userId);

    @UpdateProvider(type = SysUserMapperSQL.class,method ="updateOrderNumById")
    int updateOrderNumById(@Param("orderNum")Long orderNum, @Param("userId")Long userId);

    @SelectProvider(type = SysUserMapperSQL.class,method ="getUsersByStartEndOrder")
    List<SysUser> getUsersByStartEndOrder(@Param("startOrderNum")Long startOrderNum, @Param("endOrderNum") Long endOrderNum);

}

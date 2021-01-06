package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysLog;
import com.augurit.sys.entity.SysRole;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.entity.SysUserRole;
import com.augurit.sys.mapper.sql.SysLogMapperSQL;
import com.augurit.sys.mapper.sql.SysUserRoleMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 机构管理
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole>  {

    @SelectProvider(type = SysLogMapperSQL.class,method ="list")
    List<SysLog> list(SysLog sysLog);

    /**
     * 根据用户ID，获取角色ID列表
     */
    @SelectProvider(type = SysUserRoleMapperSQL.class,method ="queryRoleIdList")
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据用户ID，获取角色列表
     */
    @SelectProvider(type = SysUserRoleMapperSQL.class,method ="queryRoleList")
    List<SysRole> queryRoleList(Long userId);

    /**
     * yes
     * 根据userid删除
     */
    @DeleteProvider(type = SysUserRoleMapperSQL.class, method ="deleteByUserId")
    int deleteByUserId(Long userId);

    @DeleteProvider(type = SysUserRoleMapperSQL.class, method ="deleteByRoleIdUserIds")
    int deleteByRoleIdUserIds(Long roleId, Long[] userIds);

    @SelectProvider(type = SysUserRoleMapperSQL.class,method ="queryUserRole")
    List<SysUser> queryUserRole(Long roleId, String nickname);

    /**
     * 根据用户id列表或角色id列表删除
     */
    @DeleteProvider(type = SysUserRoleMapperSQL.class,method = "deleteBatch")
    int deleteBatch(List<Long> userList, List<Long> roleList);
}

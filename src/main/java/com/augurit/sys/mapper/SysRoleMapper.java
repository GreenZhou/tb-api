package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysRole;
import com.augurit.sys.mapper.sql.SysRoleMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 角色管理
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @SelectProvider(type = SysRoleMapperSQL.class,method ="list")
    List<SysRole> list(SysRole sysRole);

    /**
     * 获取该角色下的用户id集合
     */
    @SelectProvider(type = SysRoleMapperSQL.class,method ="userRoleNum")
    List<Long> userRoleNum(Long roleId, List<Long> deptIdList, String nickname);

    /**
     *	获取使用该角色名的数量
     */
    @SelectProvider(type = SysRoleMapperSQL.class,method ="getRoleCountByRoleName")
    Long getRoleCountByRoleName(String roleName);
}

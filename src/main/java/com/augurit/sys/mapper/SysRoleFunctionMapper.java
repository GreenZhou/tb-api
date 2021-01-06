package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysRoleFunction;
import com.augurit.sys.mapper.sql.SysFunctionMapperSQL;
import com.augurit.sys.mapper.sql.SysRoleFunctionMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 角色功能管理
 */
@Mapper
public interface SysRoleFunctionMapper extends BaseMapper<SysRoleFunction> {

    //根据角色ID获取功能
    @SelectProvider(type = SysRoleFunctionMapperSQL.class,method ="getByRoleId")
    List<SysRoleFunction> getByRoleId(Long roleId);

    //删除角色功能关系
    @DeleteProvider(type = SysRoleFunctionMapperSQL.class,method ="deleteByRoleFunctionIds")
    int deleteByRoleFunctionIds(Long roleId, Long[] functionIds);

}
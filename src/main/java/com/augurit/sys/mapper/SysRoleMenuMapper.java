package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysLog;
import com.augurit.sys.entity.SysRoleMenu;
import com.augurit.sys.mapper.sql.SysLogMapperSQL;
import com.augurit.sys.mapper.sql.SysRoleMenuMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 角色菜单管理
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu>  {

    @SelectProvider(type = SysLogMapperSQL.class,method ="list")
    List<SysLog> list(SysLog sysLog);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    @SelectProvider(type = SysRoleMenuMapperSQL.class,method = "queryMenuIds")
    List<Long> queryMenuIds(Long roleId);

    /**
     * 根据角色ID删除
     */
    @DeleteProvider(type = SysRoleMenuMapperSQL.class,method = "deleteByRoleId")
    int deleteByRoleId(Long roleId);

    /**
     * 根据菜单ID删除
     */
    @DeleteProvider(type = SysRoleMenuMapperSQL.class,method = "deleteByMenuId")
    int deleteByMenuId(Long menuId);

    /**
     * 根据菜单id批量删除
     */
    @DeleteProvider(type = SysRoleMenuMapperSQL.class,method = "deleteByMenuIdList")
    int deleteByMenuIdList(List<Long> menuIdList);

    /**
     * 根据角色id列表或菜单id列表删除
     */
    @DeleteProvider(type = SysRoleMenuMapperSQL.class,method = "deleteBatch")
    int deleteBatch(List<Long> roleList, List<Long> menuList);
}

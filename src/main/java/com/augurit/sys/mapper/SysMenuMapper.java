package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysMenu;
import com.augurit.sys.mapper.sql.SysMenuMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 菜单管理
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu>  {

    @SelectProvider(type = SysMenuMapperSQL.class,method ="list")
    List<SysMenu> list(SysMenu sysMenu);

    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryByObj")
    List<SysMenu> queryByObj(SysMenu sysMenu);

    /**
     * 根据唯一码查询菜单
     * @param uniqueCode
     * @return
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryByCode")
    List<SysMenu> queryByCode(String uniqueCode);
    
    /**
     * 根据菜单ID修改排序
     * @param uniqueCode
     * @return
     */
    @UpdateProvider(type = SysMenuMapperSQL.class,method ="updateByMenuId")
    int updateByMenuId(Long menuId,int orderNum);
    
    /**
     * 修改菜单状态
     * @param uniqueCode
     * @return
     */
    @UpdateProvider(type = SysMenuMapperSQL.class,method ="updateStatus")
    int updateStatus(Long[] menuIds,Integer status);

    /**
     * 根据角色ID查询菜单
     * @param roleId
     * @return
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryMenusByRole")
    List<SysMenu> queryMenusByRole(Long roleId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryListParentId")
    List<SysMenu> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryNotButtonList")
    List<SysMenu> queryNotButtonList();

    /**
     * 根据父id获取子id列表
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryRoleIdListByParentId")
    List<Long> queryRoleIdListByParentId(Long parentId);

    /**
     *
     * 根据父id获取子id列表
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryMenuIdListByParentId")
    List<Long> queryMenuIdListByParentId(Long parentId);

    /**
     *
     * 根据角色ID数组查询菜单
     */
    @SelectProvider(type = SysMenuMapperSQL.class,method ="queryMenusByRoleIds")
    List<SysMenu> queryMenusByRoleIds(@Param("roleList") List<Long> roleList);
}

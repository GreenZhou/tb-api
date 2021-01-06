package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysFunction;
import com.augurit.sys.entity.SysMenuFunction;
import com.augurit.sys.mapper.sql.SysMenuFunctionMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 菜单功能管理
 */
@Mapper
public interface SysMenuFunctionMapper extends BaseMapper<SysMenuFunction>  {

    /**
     * 根据菜单ID，获取功能列表
     */
    @SelectProvider(type = SysMenuFunctionMapperSQL.class,method = "queryFuncByMenuId")
    List<SysMenuFunction> queryFuncByMenuId(Long menuId);

    /**
     * 根据菜单ID，获取已激活的功能列表
     */
    @SelectProvider(type = SysMenuFunctionMapperSQL.class,method = "queryOpenFuncByMenuId")
    List<SysMenuFunction> queryOpenFuncByMenuId(Long roleId, Long menuId);
    
    /**
     * 根据菜单id批量删除
     */
    @DeleteProvider(type = SysMenuFunctionMapperSQL.class,method = "deleteByMenuIdList")
    int deleteByMenuIdList(List<Long> menuIdList);

    /**
     * 批量修改菜单功能状态
     */
    @UpdateProvider(type = SysMenuFunctionMapperSQL.class,method = "updateMenuFuncStatus")
	int updateMenuFuncStatus(Long[] ids,Integer mfStatus);


    /**
     * 根据菜单id和功能编码，获取功能列表
     */
    @SelectProvider(type = SysMenuFunctionMapperSQL.class,method = "queryFuncByCode")
    List<SysFunction> queryFuncByCode(Long menuId, String uniqueCode);
}

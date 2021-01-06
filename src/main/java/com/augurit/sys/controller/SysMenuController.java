package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysMenu;
import com.augurit.sys.service.SysMenuFunctionService;
import com.augurit.sys.service.SysMenuService;
import com.augurit.sys.service.SysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuFunctionService sysMenuFunctionService;

    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/page")
    public QueryResult<SysMenu> page(SysMenu sysMenu, QueryParam queryParam){
        PageResult pageResult = sysMenuService.page(sysMenu, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "删除菜单")
    @AgLog("删除菜单")
    @PostMapping("/delete")
    public QueryResult delete(@RequestParam("menuId") Long menuId){
        sysMenuService.delete(menuId);
        return QueryResult.success();
    }

    @AgLog("修改菜单")
    @ApiOperation(value = "修改菜单")
    @PostMapping("/update")
    public QueryResult update(SysMenu menu){
    	 List<SysMenu> listSysMenu = sysMenuService.queryByCode(menu.getUniqueCode());
         if (listSysMenu != null && listSysMenu.size() > 0){
        	 if (!listSysMenu.get(0).getMenuId().equals(menu.getMenuId())) {
                 return QueryResult.error("修改菜单失败,唯一码重复!");
			}
         }
        sysMenuService.updateMenu(menu);
        return QueryResult.success();
    }

    @ApiOperation(value = "新增菜单按钮")
    @AgLog("新增菜单按钮")
    @PostMapping("/save")
    public QueryResult save(SysMenu menu){
        List<SysMenu> listSysMenu = sysMenuService.queryByCode(menu.getUniqueCode());
        if (listSysMenu != null && listSysMenu.size() > 0){
            return QueryResult.error("新增菜单失败,唯一码重复!");
        }
        sysMenuService.saveMenu(menu);
        return QueryResult.success();
    }
    
    @ApiOperation(value = "更改菜单状态")
    @AgLog("更改菜单状态")
    @PostMapping("/updateMenuStatus")
    public QueryResult updateMenuStatus(@RequestParam("menuIds") String menuIds,@RequestParam("status") Integer status){
    	if (StringUtils.isNotBlank(menuIds)) {
    		String[] idsArr = menuIds.split(",");
    		Long[] menuIdArr = (Long[]) ConvertUtils.convert(idsArr,Long.class);
            sysMenuService.updateOrderNum(menuIdArr,status);
		}
        return QueryResult.success();
    }
    
    @ApiOperation(value = "更改菜单列表的排序")
    @AgLog("更改菜单列表的排序")
    @PostMapping("/updateOrderNum")
    public QueryResult updateOrderNum(@RequestParam("menuIds") String menuIds){
    	if (StringUtils.isNotBlank(menuIds)) {
    		String[] idsArr = menuIds.split(",");
    		Long[] menuIdArr = (Long[]) ConvertUtils.convert(idsArr,Long.class);
            sysMenuService.updateOrderNum(menuIdArr);
		}
        return QueryResult.success();
    }
    
    @ApiOperation(value = "根据角色ID获取菜单")
    @RequestMapping(value = "/getMenusByRole",method = RequestMethod.POST)
    public QueryResult<SysMenu> getMenusByRole(@RequestParam("roleId") Long roleId) {
    	PageResult pageResult = sysMenuService.queryMenusByRole(roleId);
    	return QueryResult.success().addPage(pageResult);
    }
    
    @ApiOperation(value = "根据菜单ID获取功能列表")
    @RequestMapping(value = "/getFuncsByMenuId",method = RequestMethod.POST)
    public QueryResult getFuncsByMenuId(@RequestParam("menuId") Long menuId) {
        PageResult pageResult = new PageResult(sysMenuFunctionService.queryFuncByMenuId(menuId));
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "根据菜单ID获取已激活的功能列表")
    @RequestMapping(value = "/getOpenFuncsByMenuId",method = RequestMethod.GET)
    public QueryResult getOpenFuncsByMenuId(Long roleId, Long menuId) {
        if(roleId == null){
            return QueryResult.error("角色ID不能为空");
        }
        PageResult pageResult = new PageResult(sysMenuFunctionService.queryOpenFuncByMenuId(roleId, menuId));
        return QueryResult.success().addPage(pageResult);
    }
    
    @ApiOperation(value = "角色授权")
    @AgLog("角色授权")
    @PostMapping("/addRoles")
    public QueryResult addRoles(SysMenu menu){
        sysRoleMenuService.saveRoleMenu(menu.getMenuId(),menu.getRoles());
        return QueryResult.success();
    }
    /**
     * 详情
     */
//    @RequestMapping("/info")
//    public PageResult info(@RequestParam("menuId") Long menuId){
//        SysMenu menu = sysMenuService.getById(menuId);
//        return QueryResult.success().addResult(menu);
//    }

    //http://localhost:8090/rbac/sys/menu/queryByObj?parentId=6&type=2
//    @RequestMapping("/queryByObj")
//    public List<SysMenu> queryByObj(SysMenu sysMenu) {
//        List<SysMenu> listSysMenu=sysMenuService.queryByObj(sysMenu);
//        return listSysMenu;
//    }

    /**
     * 导航菜单
     */
//    @RequestMapping("/nav")
//    public PageResult nav(@RequestParam("userId") Long userId){
//        List<SysMenu> menuList = sysMenuService.getUserMenuList(userId);
//        return QueryResult.success().addResult(menuList);
//    }
}

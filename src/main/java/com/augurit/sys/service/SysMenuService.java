package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.sys.entity.SysMenu;
import com.augurit.sys.entity.SysMenuFunction;
import com.augurit.sys.mapper.SysMenuMapper;
import com.github.pagehelper.PageHelper;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("sysMenuService")
public class SysMenuService extends BaseService<SysMenuMapper, SysMenu> {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuFunctionService sysMenuFunctionService;

    @Transactional(rollbackFor = Exception.class)
    public int delete(Long menuId){
        if(menuId==null){
            return 0;
        }
        //获取所有子菜单
        List<Long> menuIdList = getSubMenuIdList(menuId);
        //删除菜单
        mapper.deleteBatchIds(menuIdList);
        //删除菜单与角色关联
        sysRoleMenuService.deleteByMenuIdList(menuIdList);
        //删除菜单与功能关联
        sysMenuFunctionService.deleteByMenuIdList(menuIdList);
        return 1;
    }

    public PageResult<SysMenu> page(SysMenu sysMenu, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<SysMenu> listSysMenu = mapper.list(sysMenu);
        return new PageResult(listSysMenu);
    }

    /**
     * 根据唯一码查询菜单
     * @param uniqueCode
     * @return
     */
    public List<SysMenu> queryByCode(String uniqueCode){
        return mapper.queryByCode(uniqueCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SysMenu sysMenu){
    	sysMenu.setUpdateTime(new Date());
        this.save(sysMenu);//保存菜单信息
        String funcIds = sysMenu.getFuncIds();
        Long menuId = sysMenu.getMenuId();
        if (StringUtils.isNotBlank(funcIds)) {
	        String[] funcArr = funcIds.split(",");
	        for (String funcId : funcArr){
	            if (!"".equals(funcId)){
	                SysMenuFunction sysMenuFunction = new SysMenuFunction();
	                sysMenuFunction.setFunctionId(Long.parseLong(funcId));
	                sysMenuFunction.setMenuId(menuId);
					sysMenuFunction.setStatus(1);//默认功能状态为1:激活
	                sysMenuFunctionService.save(sysMenuFunction);//保存菜单对应的功能
	            }
	        }
        }
    }

    /**
     * 根据角色id获取菜单
     */
    public PageResult queryMenusByRole(Long roleId) {
        List<SysMenu> listSysMenu = mapper.queryMenusByRole(roleId);
        return new PageResult(listSysMenu);
    }

    /**
     * 根据角色id数组获取菜单
     */
    public List<SysMenu>  queryMenusByRoleIds(List<Long> roleList) {
        List<SysMenu> menuList = mapper.queryMenusByRoleIds(roleList);
        return menuList;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(SysMenu sysMenu){
    	sysMenu.setUpdateTime(new Date());
        this.update(sysMenu);//保存菜单信息
        long menuId = sysMenu.getMenuId();
        //先查询菜单之前功能的状态
        List<SysMenuFunction> funcList = sysMenuFunctionService.queryOpenFuncByMenuId(null, menuId);
        HashMap<Long, Integer> funcStatusMap = new HashMap<Long, Integer>();
        for (SysMenuFunction sysMenuFunction : funcList) {
			funcStatusMap.put(sysMenuFunction.getFunctionId(), sysMenuFunction.getMfStatus());
		}
        
        List<Long> menuIdList = new ArrayList<>();
        menuIdList.add(menuId);
        sysMenuFunctionService.deleteByMenuIdList(menuIdList);//删除菜单之前对应的功能
        
        String funcIds = sysMenu.getFuncIds();
        if (StringUtils.isNotBlank(funcIds)) {
        	String[] funcArr = funcIds.split(",");
            for (String funcId : funcArr){
                if (!"".equals(funcId)){
                    SysMenuFunction sysMenuFunction = new SysMenuFunction();
                    sysMenuFunction.setMenuId(menuId);
                    sysMenuFunction.setFunctionId(Long.parseLong(funcId));
                    
                    Integer status = funcStatusMap.get(Long.parseLong(funcId));
                    if (status != null) {//保存之前功能的状态
						sysMenuFunction.setStatus(status);
					}else {
						sysMenuFunction.setStatus(1);//默认功能状态为1:激活
					}
                    sysMenuFunctionService.save(sysMenuFunction);//保存菜单对应的功能
                }
            }
		}
        
    }
    /**
     *  获取本菜单及其子菜单ID列表
     */
    public List<Long> getSubMenuIdList(Long menuId){
        List<Long> menuIdList = new ArrayList<>();
        //获取子机构ID
        List<Long> subIdList = queryMenuIdListByParentId(menuId);
        getMenuTreeList(subIdList, menuIdList);
        //
        menuIdList.add(menuId);

        return menuIdList;
    }

    /**
     * 递归
     */
    private void getMenuTreeList(List<Long> subIdList, List<Long> menuIdList){
        for(Long menuId : subIdList){
            List<Long> list = queryMenuIdListByParentId(menuId);
            if(list.size() > 0){
                getMenuTreeList(list, menuIdList);
            }
            menuIdList.add(menuId);
        }
    }

    /**
     *  根据父id获取子id列表
     */
    private List<Long> queryMenuIdListByParentId(Long parentId) {
        return mapper.queryMenuIdListByParentId(parentId);
    }

    /**
     * 根据菜单ID修改排序
     * @param menuIdArr
     */
    @Transactional(rollbackFor = Exception.class)
	public void updateOrderNum(Long[] menuIdArr) {
    	int orderNum = 1;
		for (Long menuId : menuIdArr) {
			mapper.updateByMenuId(menuId, orderNum);
			orderNum++;
		}
		
	}
    
    /**
     * 修改菜单状态
     * @param menuIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderNum(Long[] menuIds,Integer status) {
    	mapper.updateStatus(menuIds, status);
    }
}

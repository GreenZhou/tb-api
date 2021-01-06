package com.augurit.sys.service;

import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysFunction;
import com.augurit.sys.entity.SysMenuFunction;
import com.augurit.sys.entity.SysRoleFunction;
import com.augurit.sys.mapper.SysMenuFunctionMapper;
import com.augurit.sys.mapper.SysRoleFunctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单与功能对应关系
 */
@Service("sysMenuFunctionService")
public class SysMenuFunctionService extends BaseService<SysMenuFunctionMapper, SysMenuFunction>{

	@Autowired
	private SysRoleFunctionService sysRoleFunctionService;

	@Autowired
	private SysRoleFunctionMapper sysRoleFunctionMapper;

	/**
	 * 根据菜单ID，获取功能
	 */
	public List<SysMenuFunction> queryFuncByMenuId(Long menuId) {
		return mapper.queryFuncByMenuId(menuId);
	}

	/**
	 * 根据菜单ID，获取已激活的功能
	 */
	public List<SysMenuFunction> queryOpenFuncByMenuId(Long roleId, Long menuId) {
		//菜单下的
		List<SysMenuFunction> sysMenuFunctions = mapper.queryOpenFuncByMenuId(roleId, menuId);
		if(roleId != null){
			//角色下的
			List<SysRoleFunction> sysRoleFunctions = sysRoleFunctionMapper.getByRoleId(roleId);
			//功能ID映射
			Map<Long,SysRoleFunction> functionMap = sysRoleFunctions.stream().collect(Collectors.toMap(SysRoleFunction::getFunctionId,sysRoleFunction -> sysRoleFunction));
			for(SysMenuFunction sysMenuFunction : sysMenuFunctions){
				if(functionMap.get(sysMenuFunction.getFunctionId()) != null){
					sysMenuFunction.setMfStatus(1);
				}else{
					sysMenuFunction.setMfStatus(0);
				}
			}
		}
		return sysMenuFunctions;
	}

	/**
	 * 根据菜单ID，删除菜单与功能的对应关系
	 */
	@Transactional(rollbackFor = Exception.class)
	public int deleteByMenuIdList(List<Long> menuIdList) {
		return mapper.deleteByMenuIdList(menuIdList);
	}

	/**
	 * 批量修改菜单功能状态
	 */
	@Transactional(rollbackFor = Exception.class)
	public QueryResult updateMenuFuncStatus(Long roleId, Long[] functionIds, Integer mfStatus) {
		if(mfStatus.equals(1)){
			//功能ID映射
			Map<Long,SysRoleFunction> functionMap = sysRoleFunctionMapper.getByRoleId(roleId).stream().collect(Collectors.toMap(SysRoleFunction::getFunctionId,sysRoleFunction -> sysRoleFunction));
			for(Long functionId : functionIds){
				if(functionMap.get(functionId) == null){
					SysRoleFunction sysRoleFunction = new SysRoleFunction();
					sysRoleFunction.setRoleId(roleId);
					sysRoleFunction.setFunctionId(functionId);
					sysRoleFunctionService.save(sysRoleFunction);
				}
			}
		}else{
			sysRoleFunctionService.deleteByRoleFunctionIds(roleId,functionIds);
		}
		return QueryResult.success();
		
	}

	/**
	 * 根据菜单id和功能编码，获取功能列表
	 */
	public List<SysFunction> queryFuncByCode(Long menuId, String uniqueCode){
		return mapper.queryFuncByCode(menuId, uniqueCode);
	}

}

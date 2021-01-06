package com.augurit.sys.service;

import com.augurit.sys.entity.SysRoleMenu;
import com.augurit.sys.mapper.SysRoleMenuMapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色与菜单对应关系
 */
@Service("sysRoleMenuService")
public class SysRoleMenuService extends BaseService<SysRoleMenuMapper, SysRoleMenu>{

	/**
	 * 根据角色ID，获取菜单ID
	 */
	public List<Long> queryMenuIdByRoleId(Long roleId) {
		return mapper.queryMenuIds(roleId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> list) {
		//先删除角色与菜单关系
		deleteByRoleId(roleId);

		if(CollectionUtils.isEmpty(list)){
			return ;
		}

		//保存角色与菜单关系
		for(Long menuId : list){
			SysRoleMenu sysRoleMenuEntity = new SysRoleMenu();
			sysRoleMenuEntity.setMenuId(menuId);
			sysRoleMenuEntity.setRoleId(roleId);

			this.save(sysRoleMenuEntity);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveRoleMenu(Long menuId, String roleIds) {
		//先删除之前的角色与菜单关系
		deleteByMenuId(menuId);

        if (StringUtils.isNotBlank(roleIds)) {
			String[] roleArr = roleIds.split(",");
			//保存最新的角色与菜单关系
			for(String roleId : roleArr){
				if (!"".equals(roleId)){
					SysRoleMenu sysRoleMenuEntity = new SysRoleMenu();
					sysRoleMenuEntity.setMenuId(menuId);
					sysRoleMenuEntity.setRoleId(Long.parseLong(roleId));
					this.save(sysRoleMenuEntity);
				}
			}
        }
	}

	public int deleteByRoleId(Long roleId){
		return mapper.deleteByRoleId(roleId);
	}

	public int deleteByMenuId(Long menuId){
		return mapper.deleteByMenuId(menuId);
	}

	public int deleteByMenuIdList(List<Long> menuIdList){
		return mapper.deleteByMenuIdList(menuIdList);
	}

	/**
	 * 根据角色id列表或菜单id列表删除
	 */
	public int deleteBatch(List<Long> roleList, List<Long> menuList) {
		return mapper.deleteBatch(roleList,menuList);
	}
}

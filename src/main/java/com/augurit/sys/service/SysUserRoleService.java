package com.augurit.sys.service;

import com.augurit.sys.entity.SysRole;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.entity.SysUserRole;
import com.augurit.sys.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系
 */
@Service("sysUserRoleService")
public class SysUserRoleService extends BaseService<SysUserRoleMapper, SysUserRole>{


	public void saveUserRoles(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		mapper.deleteByUserId(userId);
		if(roleIdList == null || roleIdList.size() == 0){
			return ;
		}
		//保存用户与角色关系
		for(Long roleId : roleIdList){
			SysUserRole sysUserRoleEntity = new SysUserRole();
			sysUserRoleEntity.setUserId(userId);
			sysUserRoleEntity.setRoleId(roleId);
			this.save(sysUserRoleEntity);
		}
	}

	public List<Long> queryRoleIdList(Long userId) {
		return mapper.queryRoleIdList(userId);
	}

	public List<SysRole> queryRoleList(Long userId) {
		return mapper.queryRoleList(userId);
	}

	public void deleteByRoleIdUserIds(Long roleId, Long[] userIds) {
		mapper.deleteByRoleIdUserIds(roleId,userIds);
	}

	public List<SysUser> queryUserRole(Long roleId, String nickname) {
		return mapper.queryUserRole(roleId,nickname);
	}

	/**
	 * 根据用户id列表或角色id列表删除
	 */
    public int deleteBatch(List<Long> userList, List<Long> roleList) {
		return mapper.deleteBatch(userList,roleList);
    }
}

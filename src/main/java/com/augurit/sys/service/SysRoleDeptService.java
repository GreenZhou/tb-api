package com.augurit.sys.service;

import com.augurit.sys.entity.SysRoleDept;
import com.augurit.sys.mapper.SysRoleDeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;


/**
 * 角色与机构对应关系
 */
@Service("sysRoleDeptService")
public class SysRoleDeptService extends BaseService<SysRoleDeptMapper, SysRoleDept> {

	/**
	 * 根据角色ID，获取机构ID
	 */
	public List<Long> queryDeptIdByRoleId(Long roleId){
		return mapper.queryDeptIdByRoleId(roleId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> list) {
		//先删除角色与机构关系
		deleteByRoleId(roleId);

		if(CollectionUtils.isEmpty(list)){
			return ;
		}

		//保存角色与菜单关系
		for(Long deptId : list){
			SysRoleDept sysRoleDeptEntity = new SysRoleDept();
			sysRoleDeptEntity.setDeptId(deptId);
			sysRoleDeptEntity.setRoleId(roleId);

			this.save(sysRoleDeptEntity);
		}
	}

	public List<Long> queryDeptIdList(Long roleIds) {
		return mapper.queryDeptIdByRoleId(roleIds);
	}

	public Set<Long> queryDeptIdList(List<Long> list) {
		return mapper.queryDeptIdList(list);
	}

//	public List<Long> queryDeptList(Long[] roleIds) {
//		return baseMapper.queryDeptIdList(roleIds);
//	}

	public int deleteBatch(List<Long> list){
		return mapper.deleteBatch(list,null);
	}

	public int deleteByRoleId(Long roleId){
		return mapper.deleteByRoleId(roleId);
	}

	/**
	 * 根据角色id列表或部门id列表删除
	 */
    public int deleteBatch(List<Long> roleList, List<Long> deptList) {
		return mapper.deleteBatch(roleList,deptList);
    }
}

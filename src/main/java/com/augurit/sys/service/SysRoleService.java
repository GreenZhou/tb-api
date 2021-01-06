package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.*;
import com.augurit.sys.mapper.SysDeptMapper;
import com.augurit.sys.mapper.SysRoleMapper;
import com.augurit.sys.mapper.SysUserMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 角色
 */
@Slf4j
@Service("sysRoleService")
public class SysRoleService extends BaseService<SysRoleMapper, SysRole>{
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysDeptMapper sysDeptMapper;

	/**
	 * 获取角色列表
	 */
	public PageResult page(SysRole sysRole, QueryParam queryParam) {
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
		List<SysRole> listSysRole = mapper.list(sysRole);
		return new PageResult(listSysRole);
	}

	/**
	 * 根据角色获取用户列表
	 */
	public PageResult userPage(Long currentDeptId, SysRole sysRole, SysUser sysUser, QueryParam queryParam) {
	    if(currentDeptId == null){
			PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
			List<SysUser> list = sysUserRoleService.queryUserRole(sysRole.getRoleId(), sysUser.getNickname());

            List<SysDept> listSysDept = sysDeptMapper.list(new SysDept());
            Map<Long,SysDept> sysDeptMap = listSysDept.stream().collect(Collectors.toMap(SysDept::getDeptId,sysDept -> sysDept));
            for (SysUser user : list) {
                if(user != null){
                    //获取结构目录
					String deptIds = user.getDeptIds();
					if(deptIds != null){
						Long[] deptIdArr = (Long[]) ConvertUtils.convert(deptIds.split(","), Long.class);
						StringBuilder deptDirectory = new StringBuilder();
						for (int i = 0; i < deptIdArr.length; i++) {
							if (i > 0) {
								deptDirectory.append("、");
							}
							Long deptId = deptIdArr[i];
							SysDept dept = sysDeptMap.get(deptId);
							Long parentId = dept.getParentId();
							if (parentId != 0 && parentId != 1) {
								deptDirectory.append(getDeptDirectory(parentId, sysDeptMap));
							}
							deptDirectory.append("/").append(dept.getName());
						}
						user.setDeptDirectory(deptDirectory.toString());
					}
				}
            }
			return new PageResult(list);
		}else {
            //获取子部门列表
            List<SysDept> subDeptList = sysDeptService.getSubDept(currentDeptId, new ArrayList<>());
            List<Long> subDeptIdList=new ArrayList<>();
            for (SysDept dept : subDeptList) {
				subDeptIdList.add(dept.getDeptId());
            }
			subDeptIdList.add(currentDeptId);
            sysUser.setSubDeptIdList(subDeptIdList);

            //获取所有用户列表
            PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
            List<SysUser> userList = sysUserMapper.list(sysUser);

            //获取该角色下的用户id集合
            List<Long> userRoleList = mapper.userRoleNum(sysRole.getRoleId(),subDeptIdList,sysUser.getNickname());
			log.debug("userRoleList:{}",userRoleList);

			List<SysDept> listSysDept = sysDeptMapper.list(new SysDept());
			Map<Long,SysDept> sysDeptMap = listSysDept.stream().collect(Collectors.toMap(SysDept::getDeptId,sysDept -> sysDept));

            //获取重合用户id
            for (SysUser user : userList) {
                if(userRoleList.contains(user.getUserId())){
                	log.debug("重合id：{}",user.getUserId());
                    user.setOpen(true);
                }
				//获取结构目录
				String deptIds = user.getDeptIds();
				if(deptIds != null){
					Long[] deptIdArr = (Long[]) ConvertUtils.convert(deptIds.split(","), Long.class);
					StringBuilder deptDirectory = new StringBuilder();
					for (int i = 0; i < deptIdArr.length; i++) {
						if (i > 0) {
							deptDirectory.append("、");
						}
						Long deptId = deptIdArr[i];
						SysDept dept = sysDeptMap.get(deptId);
						Long parentId = dept.getParentId();
						if (parentId != 0 && parentId != 1) {
							deptDirectory.append(getDeptDirectory(parentId, sysDeptMap));
						}
						deptDirectory.append("/").append(dept.getName());
					}
					user.setDeptDirectory(deptDirectory.toString());
				}
            }
            return new PageResult(userList);
        }
	}

	/**
	 * 获取用户结构目录
	 */
	public StringBuilder getDeptDirectory(Long deptId,Map<Long,SysDept> sysDeptMap){
		StringBuilder deptName = new StringBuilder();
		SysDept dept = sysDeptMap.get(deptId);
		Long parentId = dept.getParentId();
		if(parentId != 0 && parentId != 1){
			deptName.append(getDeptDirectory(parentId,sysDeptMap));
		}
		deptName.append("/").append(dept.getName());
		return deptName;
	}

	/**
	 * 用户授权
	 */
	public void authorize(Long roleId, Long[] authorizeUserIds, Long[] unauthorizeUserIds) {
		//取消授权
        if(unauthorizeUserIds != null && unauthorizeUserIds.length > 0){
            sysUserRoleService.deleteByRoleIdUserIds(roleId,unauthorizeUserIds);
        }
		//用户授权
        if(authorizeUserIds != null && authorizeUserIds.length > 0){
            for(Long userId : authorizeUserIds){
                SysUserRole ur = new SysUserRole();
                ur.setRoleId(roleId);
                ur.setUserId(userId);
                sysUserRoleService.save(ur);
            }
        }
	}

	/**
	 * 根据角色获取菜单列表
	 */
	public PageResult menuPage(SysRole sysRole) {
		List<Long> rmList = sysRoleMenuService.queryMenuIdByRoleId(sysRole.getRoleId());
		return new PageResult(rmList);
	}

	/**
	 * 菜单授权
	 */
	@Transactional(rollbackFor = Exception.class)
	public void menuAuthorize(Long roleId,Long[] menuIds) {
		//去掉所有授权
		sysRoleMenuService.deleteByRoleId(roleId);
		//用户授权
		if(menuIds!=null && menuIds.length>0){
			for(Long menuId : menuIds){
				SysRoleMenu rm =new SysRoleMenu();
				rm.setRoleId(roleId);
				rm.setMenuId(menuId);
				sysRoleMenuService.save(rm);
			}
		}
	}

	/**
	 * 删除
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] ids) {
		//获取该角色下面的所有子角色
		List<Long> roleList=new ArrayList<>(Arrays.asList(ids));
		//删除角色
		delete(ids);
		//删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleList,null);
		//删除角色与机构关联
		sysRoleDeptService.deleteBatch(roleList,null);
		//删除角色与用户关联
		sysUserRoleService.deleteBatch(null,roleList);
	}

	/**
	 * 新增
	 */
	@Transactional(rollbackFor = Exception.class)
	public QueryResult saveRole(SysRole role) {
		String roleName = role.getRoleName();
		if (mapper.getRoleCountByRoleName(roleName) > 0) {
			return QueryResult.error("该角色已存在");
		}
		role.setCreateTime(new Date());
		this.save(role);
		return QueryResult.success();
	}

	/**
	 * 修改
	 */
	@Transactional(rollbackFor = Exception.class)
	public QueryResult updateRole(SysRole role) {
		Long roleId = role.getRoleId();
		String oldRoleName = this.getById(roleId).getRoleName();
		if(oldRoleName.equals("默认角色") || oldRoleName.equals("超级管理员") || oldRoleName.equals("系统管理员")){
			return QueryResult.error("系统角色不允许修改");
		}
		String roleName = role.getRoleName();
		if (!roleName.equals(oldRoleName) && mapper.getRoleCountByRoleName(roleName) > 0) {
			return QueryResult.error("该角色已存在");
		}
		this.update(role);
		return QueryResult.success();
	}

}

package com.augurit.sys.service;


import com.augurit.common.utils.EncodeUtil;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.*;
import com.augurit.sys.mapper.SysDeptMapper;
import com.augurit.sys.mapper.SysUserMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserService extends BaseService<SysUserMapper, SysUser>{
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysUserDeptService sysUserDeptService;

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysDeptMapper sysDeptMapper;

	@Autowired
	private SysRoleService sysRoleService;

	@Transactional(rollbackFor = Exception.class)
	public void saveUser(SysUser user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(EncodeUtil.encode(user.getPassword(), salt));
		this.save(user);
		Long userId = user.getUserId();
		mapper.updateOrderNumById(userId,userId);
		//保存用户与角色关系
		sysUserRoleService.saveUserRoles(userId, user.getRoleIdList());
		//保存用户与部门关系
		sysUserDeptService.saveUserDepts(userId, user.getDeptIdList());
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateUser(SysUser user) {
		// TODO: 2020/5/22
		if(user.getLocked()!=null&&user.getLocked()==1){
			//解锁
			user.setLockedNum(0);
			user.setErrorTime(null);
		} else if(user.getLocked()!=null&&user.getLocked()==0){
			//上锁
			user.setLockedNum(4);
			user.setErrorTime(new Date());
		}

		this.update(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveUserRoles(user.getUserId(), user.getRoleIdList());
		//保存用户与部门关系
		sysUserDeptService.updateUserDepts(user.getUserId(), user.getDeptIdList());
	}

	public SysUser queryUserIdByUserName(String userName){
		return mapper.queryUserIdByUserName(userName);
	}

	public CasUser queryCasUserByUserName(String userName){
		return mapper.queryCasUserByUserName(userName);
	}

	public int updateCasUser(int locked, int lockedNum, Date errorTime, Long userId){
		return mapper.updateCasUser(locked,lockedNum,errorTime,userId);
	}

	public PageResult page(SysUser sysUser, QueryParam queryParam) {
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
		List<SysUser> listSysDept = list(sysUser);
		for(SysUser tempUser : listSysDept){
			List<Long> roleIdList=sysUserRoleService.queryRoleIdList(tempUser.getUserId());
			tempUser.setRoleIdList(roleIdList);
		}
		return new PageResult(listSysDept);
	}

	public List<SysUser> list(SysUser sysUser){
		return  mapper.list(sysUser);
	}

	/**
	 * 用户移入机构
	 */
	public void moveToDept(Long[] userIds, Long[] deptIds) {
		//保存用户与部门关系
		sysUserDeptService.moveToDept(userIds, deptIds);
	}

	/**
	 * 获取用户详细信息
	 */
	public SysUser info(Long userId) {
		SysUser sysUser = mapper.getUserWithDeptById(userId);
		if(sysUser!=null) {
			List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
			sysUser.setRoleIdList(roleIdList);
		}
		return sysUser;
	}

	/**
	 * 获取机构用户
	 */
	public List<SysUser> getUsersByDeptIds(Long[] subDeptIds,SysUser sysUser) {
		return mapper.getUsersByDeptIds(subDeptIds,sysUser);
	}

	public PageResult getSubDeptUsers(Long currentDeptId, SysUser sysUser, QueryParam queryParam) {
		List<SysUser> listUser=getSubDeptUsersList(currentDeptId,sysUser,queryParam);
		return new PageResult(listUser);
	}

	/**
	 * 获取机构下的所有用户
	 *
	 */
	public List<SysUser> getSubDeptUsersList(Long currentDeptId, SysUser sysUser, QueryParam queryParam) {
		List<SysDept> deptList=new ArrayList<SysDept>();
		sysDeptService.getSubDept(currentDeptId,deptList);//获取机构的下属机构
		//获取当前机构
		SysDept dept = sysDeptService.getById(currentDeptId);
		deptList.add(dept);
		PageHelper.startPage(queryParam.getPageNum(),queryParam.getPageSize(),queryParam.getOrderBy());
		Long[] subDeptIds = convertToArray(deptList);
		List<SysUser> userList = getUsersByDeptIds(subDeptIds,sysUser);
		//获取结构目录
		List<SysDept> listSysDept = sysDeptMapper.list(new SysDept());
		Map<Long,SysDept> sysDeptMap = listSysDept.stream().collect(Collectors.toMap(SysDept::getDeptId, sysDept -> sysDept));
		for (SysUser user : userList) {
			String deptIds = user.getDeptIds();
			if(deptIds != null){
				Long[] deptIdArr = (Long[]) ConvertUtils.convert(deptIds.split(","), Long.class);
				StringBuilder deptDirectory = new StringBuilder();
				for (int i = 0; i < deptIdArr.length; i++) {
					if (i > 0) {
						deptDirectory.append("、");
					}
					Long deptId = deptIdArr[i];
					SysDept sysDept = sysDeptMap.get(deptId);
					Long parentId = sysDept.getParentId();
					if (parentId != 0 && parentId != 1) {
						deptDirectory.append(sysRoleService.getDeptDirectory(parentId, sysDeptMap));
					}
					deptDirectory.append("/").append(sysDept.getName());
				}
				user.setDeptDirectory(deptDirectory.toString());
			}
		}
		return userList;
	}

	public Long[] convertToArray(List<SysDept> deptList){
		Long[] deptIds = new Long[deptList.size()];
		for(int i=0;i<deptList.size();i++){
			deptIds[i]=deptList.get(i).getDeptId();
		}
		return deptIds;
	}


	/**
	 * 获取机构下的所有用户数量
	 * @param deptId
	 */
	public Long getSubUsersCount(Long deptId) {
		List<SysDept> deptList=new ArrayList<SysDept>();
		sysDeptService.getSubDept(deptId,deptList);//获取下属机构
		//添加当前机构
		SysDept dept = sysDeptService.getById(deptId);
		deptList.add(dept);
		if(deptList.size()>0) {//根据机构id查询用户数量
			Long[] deptIds = convertToArray(deptList);
			return getUsersCountByDeptIds(deptIds);
		}else{
			return 0l;
		}
	}

	/**
	 * 获取机构下的用户数量
	 * @param deptIds
	 */
	public Long getUsersCountByDeptIds(Long[] deptIds) {
		return mapper.getUsersCountByDeptIds(deptIds);
	}

	/**
	 * 获取单个机构下的用户数量
	 */
	public Long getUsersCountByDeptId(Long deptId) {
		return mapper.getUsersCountByDeptId(deptId);
	}

	/**
	 * 根据用户名获取用户
	 *
	 */
	public SysUser getUserByUsername(String username) {
		return mapper.getUserByUsername(username);
	}

	/**
	 * 根据当前登录用户
	 *
	 */
	public CasUser current() {
		AttributePrincipal attributePrincipal =  AssertionHolder.getAssertion().getPrincipal();
		CasUser casUser=mapper.queryCasUserByUserName(attributePrincipal.getName());
		if(casUser!=null) {
			List<SysRole> roleList = sysUserRoleService.queryRoleList(casUser.getUserId());
			if(roleList.size()>0){
				casUser.setRoleList(roleList);
				List<Long> roleIdList = new ArrayList<>();
				roleList.stream().forEach(sysRole -> {
					roleIdList.add(sysRole.getRoleId());
				});
				List<SysMenu> menuList=sysMenuService.queryMenusByRoleIds(roleIdList);
				casUser.setMenuList(menuList);
			}
		}
		return casUser;
	}

	/**
	 * 自助修改密码
	 *
	 */
	public QueryResult updatePsw(String oldPsw, String newPsw) {
		CasUser casUser=current();
		SysUser oldUser=info(casUser.getUserId());
		String oldEncodedPsw=EncodeUtil.encode(oldPsw, oldUser.getSalt());
		if(oldEncodedPsw.equals(oldUser.getPassword())){
			SysUser newUser= new SysUser();
			newUser.setUserId(casUser.getUserId());
			String newEncodedPsw=EncodeUtil.encode(newPsw,oldUser.getSalt());
			newUser.setPassword(newEncodedPsw);
			update(newUser);
			return QueryResult.success();
		}else{
			return QueryResult.error("旧密码与原密码不匹配");
		}
	}

	/**
	 * 重置用户密码
	 *
	 */
	public void resetPsw(Long[] userIds, String newPsw) {
		for(Long userId : userIds){
			SysUser sysUser=info(userId);
			String salt=sysUser.getSalt();
			String newEncodedPsw=EncodeUtil.encode(newPsw,salt);
			sysUser.setPassword(newEncodedPsw);
			update(sysUser);
		}
	}

	/**
	 * 查询用户是否已注册
	 *
	 */
	public SysUser infoByName(String username) {
		return getUserByUsername(username);
	}

	/**
	 * 根据机构ID修改排序
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateOrderNum(Long startOrderNum, Long endOrderNum) {
		if(startOrderNum < endOrderNum){
			//往后移
			List<SysUser> sysUsers = mapper.getUsersByStartEndOrder(startOrderNum,endOrderNum);
			SysUser startUser = sysUsers.get(0);
			mapper.updateOrderNumById(endOrderNum, startUser.getUserId());
			for (int i = 1; i < sysUsers.size(); i++) {
				SysUser sysUser = sysUsers.get(i);
				Long newOrderNum = sysUser.getOrderNum() - 1;
				Long userId = sysUser.getUserId();
				mapper.updateOrderNumById(newOrderNum,userId);
			}
		}else if(startOrderNum > endOrderNum){
			//往前移
			List<SysUser> sysUsers = mapper.getUsersByStartEndOrder(endOrderNum,startOrderNum);
			int lastIndex = sysUsers.size()-1;
			SysUser lastUser = sysUsers.get(lastIndex);
			mapper.updateOrderNumById(endOrderNum, lastUser.getUserId());
			for (int i = 0; i < lastIndex; i++) {
				SysUser sysUser = sysUsers.get(i);
				Long newOrderNum = sysUser.getOrderNum() + 1;
				Long userId = sysUser.getUserId();
				mapper.updateOrderNumById(newOrderNum,userId);
			}
		}

	}
}

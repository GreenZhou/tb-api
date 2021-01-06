package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.sys.entity.SysDept;
import com.augurit.sys.mapper.SysDeptMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("sysDeptService")
public class SysDeptService extends BaseService<SysDeptMapper, SysDept> {

	@Autowired
	SysUserService sysUserService;

    @Autowired
    SysUserDeptService sysUserDeptService;

	public PageResult page(SysDept sysDept, QueryParam queryParam) {
		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
		List<SysDept> listSysDept = mapper.list(sysDept);
		//获取机构下的用户数量
		for(SysDept tempDept : listSysDept){
			Long userCount=sysUserService.getSubUsersCount(tempDept.getDeptId());
			tempDept.setCount(userCount);
			if(sysUserService.getUsersCountByDeptId(tempDept.getDeptId()) > 0){
				tempDept.setExistUser(true);
			}else{
				tempDept.setExistUser(false);
			}
		}
		return new PageResult(listSysDept);
	}

	/**
	 * 获取机构下的所有子机构
	 */
	public List<SysDept> getSubDept(Long deptId, List<SysDept> deptList) {
		//查询子机构
		SysDept subQueryDept=new SysDept();
		subQueryDept.setParentId(deptId);
 		List<SysDept> subList=mapper.list(subQueryDept);
		if(subList.size()>0) {
			deptList.addAll(subList);
			for(SysDept subDept : subList){
				getSubDept(subDept.getDeptId(),deptList);
			}
		}
		return deptList;
	}

//    public PageResult userPage(Long deptId, String nickname, QueryParam queryParam) {
//		PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
//		List<Map<String,Object>> listSysDept = mapper.userPage(deptId,nickname);
//		return new PageResult(listSysDept);
//    }

	/**
	 * 根据机构ID修改排序
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateOrderNum(Long[] deptIds, Long deptId, Long parentId) {
		int orderNum = 1;
		for (Long id : deptIds) {
			mapper.updateOrderNum(id, orderNum);
			orderNum++;
		}
		if(this.getById(deptId).getParentId() != parentId){
			//修改机构的上级机构
			mapper.updateParentId(deptId,parentId);
		}
	}

	/**
	 * 移除机构下的用户
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteUserDept(Long[] userIds, Long deptId) {
        //获取子部门列表
        List<SysDept> subDeptList = getSubDept(deptId, new ArrayList<>());
        List<Long> subDeptIdList=new ArrayList<>();
        for (SysDept dept : subDeptList) {
            subDeptIdList.add(dept.getDeptId());
        }
        subDeptIdList.add(deptId);
        sysUserDeptService.deleteUserDept(userIds,subDeptIdList);
	}
}
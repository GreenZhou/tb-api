package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysDept;
import com.augurit.sys.service.SysDeptService;
import com.augurit.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "机构管理")
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private SysUserService sysUserService;

	@ApiOperation(value = "获取机构列表")
	@RequestMapping(value="page",method = {RequestMethod.GET})
	public QueryResult<SysDept> page(SysDept sysDept, QueryParam queryParam) {
		PageResult pageResult = sysDeptService.page(sysDept,queryParam);
		return QueryResult.success().addPage(pageResult);
	}

//	@ApiOperation(value = "根据机构获取用户列表")
//	@RequestMapping(value="userPage",method = {RequestMethod.GET})
//	public QueryResult<SysDept> userPage(Long deptId, String nickname, QueryParam queryParam) {
//		PageResult pageResult = sysDeptService.userPage(deptId,nickname,queryParam);
//		return QueryResult.success().addPage(pageResult);
//	}

	@AgLog("新增机构信息")
	@ApiOperation(value = "新增机构")
	@RequestMapping(value="/save",method = {RequestMethod.POST})
	public QueryResult save(SysDept sysDept) {
		sysDeptService.save(sysDept);
		return QueryResult.success();
	}

	@AgLog("删除机构")
	@ApiOperation(value = "删除机构")
	@RequestMapping(value="/delete",method = {RequestMethod.GET})
	public QueryResult delete(Long[] deptId) {
		sysDeptService.delete(deptId);
		return QueryResult.success();
	}

	@AgLog("修改机构")
	@ApiOperation(value = "修改机构")
	@RequestMapping(value="/update",method = {RequestMethod.POST})
	public QueryResult update(SysDept sysDept) {
		sysDeptService.update(sysDept);
		return QueryResult.success();
	}

	@ApiOperation(value = "更改机构列表的排序")
	@AgLog("更改机构列表的排序")
	@GetMapping("/updateOrderNum")
	public QueryResult updateOrderNum(Long[] deptIds, Long deptId, Long parentId){
		sysDeptService.updateOrderNum(deptIds,deptId,parentId);
		return QueryResult.success();
	}

	@AgLog("移除机构下的用户")
	@ApiOperation(value = "移除机构下的用户")
	@RequestMapping(value="/deleteUserDept",method = {RequestMethod.GET})
	public QueryResult deleteUserDept(Long[] userIds, Long deptId) {
		sysDeptService.deleteUserDept(userIds,deptId);
		return QueryResult.success();
	}

}

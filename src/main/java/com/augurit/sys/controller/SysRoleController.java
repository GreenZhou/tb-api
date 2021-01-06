package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysRole;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController{
	@Autowired
	private SysRoleService sysRoleService;

    @ApiOperation(value = "获取角色列表")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
	public QueryResult<SysRole> page(SysRole sysRole, QueryParam queryParam) {
		PageResult pageResult = sysRoleService.page(sysRole, queryParam);
		return QueryResult.success().addPage(pageResult);
	}

	@ApiOperation(value = "根据角色获取用户列表")
	@RequestMapping(value = "/userPage",method = RequestMethod.GET)
	public QueryResult<SysRole> userPage(Long currentDeptId, SysRole sysRole, SysUser sysUser, QueryParam queryParam) {
		PageResult pageResult = sysRoleService.userPage(currentDeptId,sysRole,sysUser, queryParam);
		return QueryResult.success().addPage(pageResult);
	}

	@ApiOperation(value = "用户授权")
	@RequestMapping(value = "/authorize",method = RequestMethod.POST)
	public QueryResult<SysRole> authorize(Long roleId, Long[] authorizeUserIds, Long[] unauthorizeUserIds) {
		sysRoleService.authorize(roleId,authorizeUserIds,unauthorizeUserIds);
		return QueryResult.success();
	}

    @ApiOperation(value = "根据角色获取菜单列表")
    @RequestMapping(value = "/menuPage",method = RequestMethod.GET)
    public QueryResult<SysRole> menuPage(SysRole sysRole) {
        PageResult pageResult = sysRoleService.menuPage(sysRole);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "菜单功能授权")
    @RequestMapping(value = "/menuAuthorize",method = RequestMethod.POST)
    public QueryResult<SysRole> menuAuthorize(Long roleId, Long[] menuIds) {
        sysRoleService.menuAuthorize(roleId,menuIds);
        return QueryResult.success();
    }

    @ApiOperation(value = "新增角色")
    @AgLog("新增角色")
	@RequestMapping(value="/save",method= RequestMethod.POST)
	public QueryResult save(SysRole role){
		QueryResult queryResult = sysRoleService.saveRole(role);
		return queryResult;
	}

    @ApiOperation(value = "修改角色")
    @AgLog("修改角色")
	@RequestMapping(value="/update",method= RequestMethod.POST)
	public QueryResult update(SysRole role){
		QueryResult queryResult = sysRoleService.updateRole(role);
		return queryResult;
	}

	@ApiOperation(value = "删除角色")
	@AgLog("删除角色")
	@RequestMapping(value="/delete",method = RequestMethod.GET)
	public QueryResult delete(Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		return QueryResult.success();
	}

}

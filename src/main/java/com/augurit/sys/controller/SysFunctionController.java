package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysFunction;
import com.augurit.sys.entity.SysMenuFunction;
import com.augurit.sys.service.SysFunctionService;
import com.augurit.sys.service.SysMenuFunctionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "功能管理")
@RestController
@RequestMapping("/sys/function")
public class SysFunctionController {

    @Autowired
    private SysFunctionService sysFunctionService;

    @Autowired
    private SysMenuFunctionService sysMenuFunctionService;
    
    @ApiOperation(value = "获取功能列表")
    @GetMapping("/page")
    public QueryResult<SysFunction> page(SysFunction sysFunction, QueryParam queryParam){
        PageResult pageResult = sysFunctionService.page(sysFunction, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "删除功能")
    @AgLog("删除功能")
    @PostMapping("/delete")
    public QueryResult delete(@RequestParam("functionId") Long functionId,@RequestParam("id") Long id){
        sysFunctionService.deleteFunction(functionId,id);
        return QueryResult.success();
    }

    @AgLog("修改功能")
    @ApiOperation(value = "修改功能")
    @PostMapping("/update")
    public QueryResult update(SysFunction sysFunction, Long menuId){
        QueryResult queryResult = sysFunctionService.updateFunction(sysFunction,menuId);
        return queryResult;
    }

    @ApiOperation(value = "新增功能")
    @AgLog("新增功能")
    @PostMapping("/save")
    public QueryResult save(SysFunction sysFunction, Long menuId){
        QueryResult queryResult = sysFunctionService.saveFunction(sysFunction,menuId);
        return queryResult;
    }

    @AgLog("修改菜单中功能的状态")
    @ApiOperation(value = "修改菜单中功能的状态")
    @PostMapping("/updateMenuFuncStatus")
    public QueryResult updateMenuFuncStatus(Long roleId, Long[] functionIds, Integer mfStatus){
        if(roleId == null){
            return QueryResult.error("角色ID不能为空");
        }
        sysMenuFunctionService.updateMenuFuncStatus(roleId, functionIds, mfStatus);
        return QueryResult.success();
    }
}

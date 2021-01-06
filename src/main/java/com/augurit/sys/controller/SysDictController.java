package com.augurit.sys.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysDict;
import com.augurit.sys.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "字典管理")
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    SysDictService sysDictService;

    @ApiOperation(value = "获取字典列表")
    @GetMapping("/page")
    public QueryResult<SysDict> page(SysDict sysDict, QueryParam queryParam) {
        PageResult pageResult = sysDictService.page(sysDict,queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @AgLog("新增字典")
    @ApiOperation(value = "新增字典")
    @PostMapping("/save")
    public QueryResult save(SysDict sysDict) {
        sysDictService.save(sysDict);
        return QueryResult.success();
    }

    @AgLog("删除字典")
    @ApiOperation(value = "删除字典")
    @GetMapping("/delete")
    public QueryResult delete(Long[] dictId) {
        sysDictService.deleteDict(dictId);
        return QueryResult.success();
    }

    @AgLog("修改字典")
    @ApiOperation(value = "修改字典")
    @PostMapping("/update")
    public QueryResult update(SysDict sysDict) {
        sysDictService.update(sysDict);
        return QueryResult.success();
    }
}

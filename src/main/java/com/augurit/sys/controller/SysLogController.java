package com.augurit.sys.controller;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysDept;
import com.augurit.sys.entity.SysLog;
import com.augurit.sys.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "日志管理")
@RestController
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

    @ApiOperation(value = "获取日志列表")
    @RequestMapping(value = "page", method = {RequestMethod.GET})
    public QueryResult<SysDept> page(SysLog sysLog, QueryParam queryParam) {
        PageResult pageResult = sysLogService.page(sysLog, queryParam);

        return QueryResult.success().addPage(pageResult);
    }

}

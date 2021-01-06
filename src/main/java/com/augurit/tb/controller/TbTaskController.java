package com.augurit.tb.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.service.SysUserService;
import com.augurit.tb.entity.TbTask;
import com.augurit.tb.service.TbTaskService;
import com.augurit.tb.util.SimpleNumberGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags ="任务管理")
@RestController
@RequestMapping("/tb/task")
public class TbTaskController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TbTaskService tbTaskService;

    @ApiOperation(value = "获取所属任务列表")
    @RequestMapping(value = "page", method = {RequestMethod.GET})
    public QueryResult<TbTask> page(String name, String status, QueryParam queryParam) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        PageResult pageResult = tbTaskService.page(name, status, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "删除指定任务")
    @AgLog("删除指定任务")
    @PostMapping("/delete")
    public QueryResult delete(@RequestParam("id") String id){
        tbTaskService.delete(id);
        return QueryResult.success();
    }

    @ApiOperation(value = "新增任务")
    @AgLog("新增任务")
    @PostMapping("/save")
    public QueryResult save(TbTask task) {
        CasUser loginUser = sysUserService.current();
        task.setId(DefaultIdGenerator.getIdForStr());
        task.setTaskName(SimpleNumberGenerator.generate());
        task.setCreateTime(new Date());
        task.setCreatorId(loginUser.getUserId());
        task.setCreatorName(loginUser.getUserName());
        tbTaskService.save(task);
        return QueryResult.success();
    }

    @ApiOperation(value = "发布任务")
    @AgLog("新增任务")
    @PostMapping("/publish")
    public QueryResult publish(String id) {
        CasUser loginUser = sysUserService.current();
        tbTaskService.publish(id, loginUser.getUserId());
        return QueryResult.success();
    }
}

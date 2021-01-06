package com.augurit.tb.service;

import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.entity.TbTask;
import com.augurit.tb.mapper.TbSubTaskMapper;
import com.augurit.tb.mapper.TbTaskMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbTaskService")
public class TbTaskService {
    // 待发布
    public static final String TASK_STATUS_NOT_PUBLISH = "0";
    // 待分配
    public static final String TASK_STATUS_PUBLISHED = "1";
    // 待核查
    public static final String TASK_STATUS_ALLOCATED = "2";
    // 已完成
    public static final String TASK_STATUS_CHECKED = "3";

    @Autowired
    private TbTaskMapper mapper;

    @Autowired
    private TbSubTaskMapper subTaskmapper;

    public PageResult page(String name, String status, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<TbTask> tasks = mapper.listTasks(name, status);
        return new PageResult(tasks);
    }

    public void delete(String id) {
    }

    public void save(TbTask task) {
        mapper.saveTask(task);

        // 给任务分配子任务
        TbSubTask subTask = new TbSubTask();
        subTask.setId(DefaultIdGenerator.getIdForStr());
        subTask.setPid(task.getId());
        subTask.setSubTaskName("早班");

        subTaskmapper.saveSubTask(subTask);
        subTask.setId(DefaultIdGenerator.getIdForStr());
        subTask.setSubTaskName("晚班");
        subTaskmapper.saveSubTask(subTask);

    }

    // 任务发布 更新任务状态
    public void publish(String id, Long userId) {
        TbTask task = new TbTask();
        task.setId(id);
        task.setCreatorId(userId);
        task.setStatus(TbTaskService.TASK_STATUS_PUBLISHED);
        mapper.updateTask(task);
    }

    // 任务分配 更新任务状态
    public void allocate(String id) {
        TbTask task = new TbTask();
        task.setId(id);
        task.setStatus(TbTaskService.TASK_STATUS_ALLOCATED);
        mapper.updateTask(task);
    }

    // 任务核查 更新任务状态
    public void check(String id) {
        TbTask task = new TbTask();
        task.setId(id);
        task.setStatus(TbTaskService.TASK_STATUS_CHECKED);
        mapper.updateTask(task);
    }
}

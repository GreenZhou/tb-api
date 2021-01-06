package com.augurit.tb.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.entity.SysUser;
import com.augurit.tb.entity.TbBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.entity.TbSubTaskEmp;
import com.augurit.tb.mapper.TbSubTaskMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbSubTaskService")
public class TbSubTaskService {

    public static final String BUYER_STATUS_CHECKED = "1";

    public static final String EMP_STATUS_CHECKED = "2";

    @Autowired
    private TbSubTaskMapper mapper;

    public PageResult page(String pid, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<TbSubTask> tasks = mapper.listSubTasks(pid);
        tasks.forEach(task -> {
            task.setEmps(mapper.listSubTaskEmps(task.getId()));
        });
        return new PageResult(tasks);
    }

    public List<SysUser> listEmps(Long creatorId) {
        return mapper.listEmps(creatorId);
    }

    public void assignEmps(String tsid, List<TbSubTaskEmp> subTaskEmps) {
        mapper.removeAssignedEmps(tsid);
        mapper.assignEmps(subTaskEmps);
    }

    public List<TbSubTask> listEmpAllocatedSubTasks(Long userId) {
        return mapper.listEmpSubTasks(null, userId, TbTaskService.TASK_STATUS_PUBLISHED);
    }

    public void saveBuyerBatch(List<TbBuyer> buyers, String tsid, Long userId) {
        mapper.removeBuyers(tsid, userId);
        mapper.saveBuyerBatch(buyers);
    }

    public PageResult listBuyersPage(String tsid, Long userId, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<TbBuyer> buyers = mapper.listBuyers(tsid, userId);
        return new PageResult(buyers);
    }

    public void deleteBuyers(String tsid, Long userId) {
        mapper.deleteBuyers(tsid, userId);
    }

    public void checkBuyers(String tsid, Long userId) {
        TbSubTaskEmp subTaskEmp = new TbSubTaskEmp();
        subTaskEmp.setTsid(tsid);
        subTaskEmp.setCheckStatus(TbSubTaskService.EMP_STATUS_CHECKED);
        subTaskEmp.setEmpId(userId);
        mapper.updateSubTaskEmp(subTaskEmp);

        TbBuyer buyer = new TbBuyer();
        buyer.setTsid(tsid);
        buyer.setCreatorId(userId);
        buyer.setStatus(TbSubTaskService.BUYER_STATUS_CHECKED);
        mapper.updateBuyer(buyer);
    }
}

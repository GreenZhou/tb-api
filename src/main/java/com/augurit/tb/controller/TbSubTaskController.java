package com.augurit.tb.controller;

import com.augurit.common.annotation.AgLog;
import com.augurit.common.exception.AGException;
import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.entity.SysUser;
import com.augurit.sys.service.SysUserService;
import com.augurit.tb.entity.*;
import com.augurit.tb.poi.TbBuyerProcessor;
import com.augurit.tb.poi.TbOrderProcessor;
import com.augurit.tb.service.TbSubTaskService;
import com.augurit.tb.service.TbTaskService;
import com.augurit.tb.util.SimpleNumberGenerator;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Api(tags ="子任务管理")
@RestController
@RequestMapping("/tb/subTask")
@Slf4j
public class TbSubTaskController {

    private static final String EMP_NULL_CHECK_STATUS = "0";
    private static final String EMP_NOT_CHECK_STATUS = "1";
    private static final String EMP_YES_CHECK_STATUS = "2";

    @Value("${upload-path-default}")
    private String filePath;

    private static final TbBuyerProcessor processor = new TbBuyerProcessor();

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TbSubTaskService tbSubTaskService;

    @ApiOperation(value = "获取所属子任务列表")
    @RequestMapping(value = "page", method = {RequestMethod.GET})
    public QueryResult<TbTask> page(String pid, QueryParam queryParam) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        PageResult pageResult = tbSubTaskService.page(pid, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "获取员工列表")
    @RequestMapping(value = "listEmps", method = {RequestMethod.GET})
    public QueryResult<TbTask> listEmps() {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        List<SysUser> pageResult = tbSubTaskService.listEmps(loginUser.getUserId());
        return QueryResult.success().addList(pageResult);
    }

    @ApiOperation(value = "指派员工")
    @RequestMapping(value = "assignEmps", method = {RequestMethod.POST})
    public QueryResult assignEmps(String empIds, String tsid, String empNames) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        String[] empIdList = empIds.split(",");
        String[] empNameList = empNames.split(",");

        List<TbSubTaskEmp> subTaskEmps = Lists.newArrayList();
        for(int i = 0; i < empIdList.length; i++) {
            TbSubTaskEmp subTaskEmp =  new TbSubTaskEmp();
            subTaskEmp.setId(DefaultIdGenerator.getIdForStr());
            subTaskEmp.setTsid(tsid);
            subTaskEmp.setEmpId(Long.parseLong(empIdList[i]));
            subTaskEmp.setEmpName(empNameList[i]);
            subTaskEmp.setCheckStatus(TbSubTaskController.EMP_NOT_CHECK_STATUS);

            subTaskEmps.add(subTaskEmp);
        }

        tbSubTaskService.assignEmps(tsid, subTaskEmps);

        return QueryResult.success();
    }

    @ApiOperation(value = "指派的子任务列表查询")
    @RequestMapping(value = "listEmpSubTasks", method = {RequestMethod.GET})
    public QueryResult<TbTask> listEmpSubTasks() {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        List<TbSubTask> subTasks = tbSubTaskService.listEmpAllocatedSubTasks(loginUser.getUserId());
        for(TbSubTask subTask : subTasks) {
            QueryParam param = new QueryParam();
            param.setPageNum(1);
            param.setPageSize(Integer.MAX_VALUE);
            PageResult<TbBuyer> buyers = tbSubTaskService.listBuyersPage(subTask.getId(), loginUser.getUserId(), param);
            subTask.setUploadedBuyerNum(buyers.getTotal());
        }
        return QueryResult.success().addList(subTasks);
    }

    @ApiOperation(value = "买家列表查询")
    @RequestMapping(value = "listBuyersPage", method = {RequestMethod.GET})
    public QueryResult<TbTask> listBuyersPage(String tsid, QueryParam queryParam) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        PageResult pageResult = tbSubTaskService.listBuyersPage(tsid, loginUser.getUserId(), queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "导入买家")
    @PostMapping("/importBuyers/{tsid}")
    public void importOrder(@RequestParam("file") MultipartFile file,
                            @PathVariable("tsid") String tsid, HttpServletResponse resp) throws AGException {
        try {
            String originalName = file.getOriginalFilename();
            String suffix = originalName.substring(originalName.lastIndexOf("."));
            // 上传文件名
            String newFilename = DefaultIdGenerator.getIdForStr() + suffix;
            File serverTempFile = new File(filePath + "temp/" + newFilename);
            // 将上传的文件写入到服务器端文件内
            file.transferTo(serverTempFile);
            List<TbBuyer> buyers = processor.process(serverTempFile);
            CasUser loginUser = sysUserService.current();
            buyers.forEach(buyer -> {
                buyer.setCreatorId(loginUser.getUserId());
                buyer.setTsid(tsid);
            });

            tbSubTaskService.deleteBuyers(tsid, loginUser.getUserId());
            tbSubTaskService.saveBuyerBatch(buyers, tsid, loginUser.getUserId());

        } catch (Exception e) {
            log.error("买家导入失败", e);
            throw new AGException("买家导入失败");
        }
    }

    @ApiOperation(value = "买家核查")
    @RequestMapping(value = "checkBuyers", method = {RequestMethod.GET})
    public QueryResult checkBuyers(String tsid) {
        CasUser loginUser = sysUserService.current();
        tbSubTaskService.checkBuyers(tsid, loginUser.getUserId());
        return QueryResult.success();
    }
}

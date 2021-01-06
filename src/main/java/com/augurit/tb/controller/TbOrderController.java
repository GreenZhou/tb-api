package com.augurit.tb.controller;

import com.augurit.common.exception.AGException;
import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.CasUser;
import com.augurit.sys.service.SysUserService;
import com.augurit.tb.compress.IUncompress;
import com.augurit.tb.compress.UncompressImpl;
import com.augurit.tb.entity.TbFileInfo;
import com.augurit.tb.entity.TbOrder;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.entity.TbTask;
import com.augurit.tb.poi.TbOrderProcessor;
import com.augurit.tb.service.TbFileService;
import com.augurit.tb.service.TbOrderService;
import com.augurit.tb.service.TbSubTaskService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Api(tags ="订单管理")
@RestController
@RequestMapping("/tb/order")
@Slf4j
public class TbOrderController {

    private final static IUncompress compress = new UncompressImpl();

    @Value("${upload-path-default}")
    private String filePath;

    @Value("${uncompress-dest-path-default}")
    private String uncompressDestPath;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private TbFileService tbFileService;

    @Autowired
    private TbSubTaskService tbSubTaskService;

    @Autowired
    private TbOrderService tbOrderService;

    @ApiOperation(value = "导入订单任务")
    @PostMapping("/import/{taskId}")
    public void importOrder(@RequestParam("file") MultipartFile file,
                            @PathVariable("taskId") String taskId, HttpServletResponse resp) throws AGException {
        try {
            String originalName = file.getOriginalFilename();
            // IE8下会拿到文件的路径名
            if(originalName.indexOf("\\") != -1) {// windows环境
                originalName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            }

            if(originalName.indexOf("/") != -1) {
                originalName = originalName.substring(originalName.lastIndexOf("/") + 1);
            }
            String suffix = originalName.substring(originalName.lastIndexOf("."));
            String id = DefaultIdGenerator.getIdForStr();
            // 上传文件名
            String newFilename = id + suffix;
            File serverFile = new File(filePath + newFilename);
            // 将上传的文件写入到服务器端文件内
            file.transferTo(serverFile);

            TbFileInfo fileInfo = new TbFileInfo();
            fileInfo.setId(id);
            fileInfo.setOriginalName(originalName);
            fileInfo.setSuffix(suffix);
            fileInfo.setDirPath(filePath + taskId);
            fileInfo.setCreateTime(new Date());

            CasUser loginUser = sysUserService.current();

            fileInfo.setCreatorId(loginUser.getUserId());
            fileInfo.setCreatorName(loginUser.getUserName());
            tbFileService.saveFile(fileInfo);

            // 解压
            compress.uncompress(serverFile.getCanonicalPath(), uncompressDestPath + taskId);

            // 取xls文件
            // 解析xls文件并数据入库
            List<TbOrder> orders = Lists.newArrayList();

            QueryParam param = new QueryParam();
            param.setPageNum(1);
            param.setPageSize(100);
            PageResult pr = tbSubTaskService.page(taskId, param);
            List<TbSubTask> subTasks = pr.getList();

            Iterator<File> files = FileUtils.iterateFiles(new File(uncompressDestPath + taskId), new String[] {"xls", "xlsx"}, true);
            while(files.hasNext()) {
                File xlsFile = files.next();
                // 解析excel
                List<TbOrder> xlsOrders = new TbOrderProcessor().process(xlsFile, subTasks);
                orders.addAll(xlsOrders);
            }

            tbOrderService.saveBatch(orders, taskId);

        } catch (Exception e) {
            log.error("任务详情导入失败", e);
            throw new AGException("任务详情导入失败");
        }
    }

    @ApiOperation(value = "获取表订单列表")
    @RequestMapping(value = "page", method = {RequestMethod.GET})
    public QueryResult<TbOrder> page(String taskId, String tsids, QueryParam queryParam) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        List<String> tsidList = Strings.isNotBlank(tsids) ? Lists.newArrayList(tsids.split(",")) : null;
        PageResult pageResult = tbOrderService.page(taskId, tsidList, queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "订单-买家统计列表查询")
    @RequestMapping(value = "listOrderBuyersPage", method = {RequestMethod.GET})
    public QueryResult<TbTask> listOrderBuyersPage(String tsid, QueryParam queryParam) {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        PageResult pageResult = tbOrderService.listOrderBuyersPage(tsid, loginUser.getUserId(), queryParam);
        return QueryResult.success().addPage(pageResult);
    }

    @ApiOperation(value = "生成原始订单核查表")
        @RequestMapping(value = "generateOrderBuyerExcel/{tsid}", method = {RequestMethod.GET})
    public void generateOrderBuyerExcel(@PathVariable("tsid") String tsid, HttpServletResponse resp) throws Exception {
        CasUser loginUser = sysUserService.current();
        System.out.println(loginUser == null ? "未登录" : loginUser.getUserId());
        tbOrderService.generateOrderBuyerExcel(tsid, loginUser.getUserId(), resp);
    }
}

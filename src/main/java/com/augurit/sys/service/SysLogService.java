package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.sys.entity.SysLog;
import com.augurit.sys.mapper.SysLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("sysLogService")
public class SysLogService extends BaseService<SysLogMapper, SysLog>{

    public PageResult page(SysLog sysLog, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<SysLog> listSysLog = mapper.list(sysLog);
        return new PageResult(listSysLog);
    }
}

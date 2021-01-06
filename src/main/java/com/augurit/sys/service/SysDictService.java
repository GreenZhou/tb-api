package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.sys.entity.SysDict;
import com.augurit.sys.mapper.SysDictMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("sysDictService")
public class SysDictService extends BaseService<SysDictMapper, SysDict>{

    public PageResult page(SysDict sysDict, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<SysDict> listSysDict = mapper.list(sysDict);
        return new PageResult(listSysDict);
    }

    public void deleteByParentId(Long parentId) {
         mapper.deleteByParentId(parentId);
    }

    public void deleteDict(Long[] dictId) {
        for(Long id : dictId){
            deleteById(id);
            deleteByParentId(id);
        }
    }
}

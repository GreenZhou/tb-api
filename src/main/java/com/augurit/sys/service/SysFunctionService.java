package com.augurit.sys.service;

import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.common.utils.QueryResult;
import com.augurit.sys.entity.SysFunction;
import com.augurit.sys.entity.SysMenuFunction;
import com.augurit.sys.mapper.SysFunctionMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysFunctionService")
public class SysFunctionService extends BaseService<SysFunctionMapper, SysFunction> {

    @Autowired
    private SysMenuFunctionService sysMenuFunctionService;

    public PageResult<SysFunction> page(SysFunction sysFunction, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<SysFunction> listSysFunction = mapper.list(sysFunction);
        return new PageResult(listSysFunction);
    }

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public QueryResult saveFunction(SysFunction sysFunction,Long menuId) {
        List<SysFunction> sysFunctions = sysMenuFunctionService.queryFuncByCode(menuId,sysFunction.getUniqueCode());
        if (sysFunctions != null && sysFunctions.size() > 0){
            return QueryResult.error("新增功能失败,功能编码重复!");
        }
        this.save(sysFunction);
        SysMenuFunction mf = new SysMenuFunction();
        mf.setMenuId(menuId);
        mf.setFunctionId(sysFunction.getFunctionId());
        sysMenuFunctionService.save(mf);
        return QueryResult.success();
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public QueryResult updateFunction(SysFunction sysFunction,Long menuId) {
        List<SysFunction> sysFunctions = sysMenuFunctionService.queryFuncByCode(menuId,sysFunction.getUniqueCode());
        if (sysFunctions != null && sysFunctions.size() > 0){
            if (!sysFunctions.get(0).getFunctionId().equals(sysFunction.getFunctionId())) {
                return QueryResult.error("修改菜单失败,功能编码重复!");
            }
        }
        this.update(sysFunction);
        return QueryResult.success();
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long functionId, Long id) {
        sysMenuFunctionService.deleteById(id);
        this.deleteById(functionId);
    }
}

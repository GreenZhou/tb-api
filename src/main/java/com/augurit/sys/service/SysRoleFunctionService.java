package com.augurit.sys.service;

import com.augurit.sys.entity.SysRoleFunction;
import com.augurit.sys.mapper.SysRoleFunctionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色与功能对应关系
 */
@Service("sysRoleFunctionService")
public class SysRoleFunctionService extends BaseService<SysRoleFunctionMapper, SysRoleFunction>{

    /**
     * 根据角色ID获取功能
     */
    public List<SysRoleFunction> getByRoleId(Long roleId) {
        return mapper.getByRoleId(roleId);
    }

    /**
     * 删除角色功能关系
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleFunctionIds(Long roleId, Long[] functionIds) {
        return mapper.deleteByRoleFunctionIds(roleId,functionIds);
    }

}

package com.augurit.sys.service;

import com.augurit.sys.entity.SysUserDept;
import com.augurit.sys.mapper.SysUserDeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户与机构对应关系
 */
@Service("sysUserDeptService")
public class SysUserDeptService extends BaseService<SysUserDeptMapper, SysUserDept>{

    @Transactional(rollbackFor = Exception.class)
    public void saveUserDepts(Long userId, List<Long> deptIdList) {
        if(deptIdList == null || deptIdList.size() == 0){
            return ;
        }
        //保存用户与机构关系
        for(Long deptId : deptIdList){
            SysUserDept sysUserDept = new SysUserDept();
            sysUserDept.setUserId(userId);
            sysUserDept.setDeptId(deptId);
            this.save(sysUserDept);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserDepts(Long userId, List<Long> deptIdList) {
        //先删除用户与机构关系
        mapper.deleteByUserId(userId);
        //保存用户与机构关系
        saveUserDepts(userId, deptIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void moveToDept(Long[] userIds, Long[] deptIds) {
        //先删除所选用户与机构关系
        mapper.deleteByUserIds(userIds);
        //保存用户与机构关系
        for(Long userId : userIds){
            for(Long deptId : deptIds){
                SysUserDept sysUserDept = new SysUserDept();
                sysUserDept.setUserId(userId);
                sysUserDept.setDeptId(deptId);
                this.save(sysUserDept);
            }
        }
    }

    /**
     * 移除机构下的用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserDept(Long[] userIds, List<Long> subDeptIdList) {
        mapper.deleteByUserIdDeptId(userIds,subDeptIdList);
    }

}

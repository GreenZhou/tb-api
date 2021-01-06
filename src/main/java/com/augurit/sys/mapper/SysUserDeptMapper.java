package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysUserDept;
import com.augurit.sys.mapper.sql.SysUserDeptMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户机构管理
 */
@Mapper
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    /**
     * 根据角色ID，批量删除
     */
    @DeleteProvider(type = SysUserDeptMapperSQL.class, method ="deleteByUserId")
    int deleteByUserId(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    @DeleteProvider(type = SysUserDeptMapperSQL.class, method ="deleteByUserIds")
    int deleteByUserIds(@Param("userIds") Long[] userIds);

    /**
     * 移除机构下的用户
     */
    @DeleteProvider(type = SysUserDeptMapperSQL.class, method ="deleteByUserIdDeptId")
    int deleteByUserIdDeptId(@Param("userIds") Long[] userIds,@Param("subDeptIdList") List<Long> subDeptIdList);
}

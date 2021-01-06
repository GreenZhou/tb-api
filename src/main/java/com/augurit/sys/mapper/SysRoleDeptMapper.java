package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysLog;
import com.augurit.sys.entity.SysRoleDept;
import com.augurit.sys.mapper.sql.SysLogMapperSQL;
import com.augurit.sys.mapper.sql.SysRoleDeptMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Set;

/**
 * 机构管理
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept>  {

    @SelectProvider(type = SysLogMapperSQL.class,method ="list")
    List<SysLog> list(SysLog sysLog);

    /**
     * 根据角色ID，获取机构ID
     */
    @SelectProvider(type = SysRoleDeptMapperSQL.class,method ="queryDeptIdByRoleId")
    List<Long> queryDeptIdByRoleId(Long roleId);

    /**
     * 根据角色ID，获取机构ID列表
     */
    @SelectProvider(type = SysRoleDeptMapperSQL.class,method ="queryDeptIdList")
    Set<Long> queryDeptIdList(List<Long> list);

    /**
     * 根据角色ID删除
     */
    @DeleteProvider(type = SysRoleDeptMapperSQL.class,method = "deleteByRoleId")
    int deleteByRoleId(Long roleId);

    /**
     * 根据角色id列表或部门id列表删除
     */
    @DeleteProvider(type = SysRoleDeptMapperSQL.class,method = "deleteBatch")
    int deleteBatch(List<Long> roleList, List<Long> deptList);
}

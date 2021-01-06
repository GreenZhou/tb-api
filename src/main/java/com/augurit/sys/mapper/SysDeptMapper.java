package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysDept;
import com.augurit.sys.mapper.sql.SysDeptMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 机构管理
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept>  {

    @SelectProvider(type = SysDeptMapperSQL.class,method ="list")
    List<SysDept> list(SysDept sysDept);

//    @SelectProvider(type = SysDeptMapperSQL.class,method ="userPage")
//    List<Map<String,Object>> userPage(Long deptId, String nickname);

    @UpdateProvider(type = SysDeptMapperSQL.class,method ="updateOrderNum")
    int updateOrderNum(@Param("deptId") Long deptId,@Param("orderNum") int orderNum);

    @UpdateProvider(type = SysDeptMapperSQL.class,method ="updateParentId")
    int updateParentId(@Param("deptId") Long deptId,@Param("parentId") Long parentId);
}

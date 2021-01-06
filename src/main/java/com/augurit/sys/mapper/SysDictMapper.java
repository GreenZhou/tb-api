package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysDict;
import com.augurit.sys.mapper.sql.SysDictMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 机构管理
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict>  {

    @SelectProvider(type = SysDictMapperSQL.class,method ="list")
    List<SysDict> list(SysDict sysDict);

    @DeleteProvider(type = SysDictMapperSQL.class,method ="deleteByParentId")
    int deleteByParentId(Long parentId);
}

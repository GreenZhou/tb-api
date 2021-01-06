package com.augurit.sys.mapper;

import com.augurit.sys.entity.SysFunction;
import com.augurit.sys.mapper.sql.SysFunctionMapperSQL;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 功能管理
 */
@Mapper
public interface SysFunctionMapper extends BaseMapper<SysFunction>  {

    @SelectProvider(type = SysFunctionMapperSQL.class,method ="list")
    List<SysFunction> list(SysFunction sysFunction);

}

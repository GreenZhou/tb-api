package com.augurit.sys.mapper;

import com.augurit.sys.mapper.sql.SysLogMapperSQL;
import com.augurit.sys.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 机构管理
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog>  {

    @SelectProvider(type = SysLogMapperSQL.class,method ="list")
    List<SysLog> list(SysLog sysLog);
}

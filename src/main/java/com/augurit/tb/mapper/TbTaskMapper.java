package com.augurit.tb.mapper;

import com.augurit.tb.entity.TbTask;
import com.augurit.tb.mapper.sql.TbTaskMapperSQL;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface TbTaskMapper {
    @SelectProvider(type = TbTaskMapperSQL.class, method ="listTasks")
    List<TbTask> listTasks(String name, String status);

    @InsertProvider(type = TbTaskMapperSQL.class, method ="saveTask")
    void saveTask(TbTask task);

    @UpdateProvider(type = TbTaskMapperSQL.class, method ="updateTask")
    void updateTask(TbTask task);
}

package com.augurit.tb.mapper;

import com.augurit.tb.entity.TbFileInfo;
import com.augurit.tb.mapper.sql.TbFileMapperSQL;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TbFileMapper {
    @InsertProvider(type = TbFileMapperSQL.class, method ="saveFile")
    void saveFile(TbFileInfo fileInfo);

    @UpdateProvider(type = TbFileMapperSQL.class, method ="updateFileStatus")
    void updateFileStatus(String id, String status);

    @SelectProvider(type = TbFileMapperSQL.class, method ="listFileInfos")
    List<TbFileInfo> listFileInfos(List<String> ids);

    @SelectProvider(type = TbFileMapperSQL.class, method ="getFileInfo")
    TbFileInfo getFileInfo(String id);

    @DeleteProvider(type = TbFileMapperSQL.class, method ="removeFiles")
    void removeFiles(List<String> ids);
}

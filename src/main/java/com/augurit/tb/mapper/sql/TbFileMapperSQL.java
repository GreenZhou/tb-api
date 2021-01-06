package com.augurit.tb.mapper.sql;

import com.augurit.tb.entity.TbFileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class TbFileMapperSQL {
    public String saveFile(TbFileInfo fileInfo){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_file(id, suffix, original_name, status, create_time, dir_path, creator_id, creator_name)");
        sb.append(" values(#{id}, #{suffix}, #{originalName}, #{status}, #{createTime}, #{dirPath}, #{creatorId}, #{creatorName})");
        return sb.toString();
    }

    public String updateFileStatus(@Param("id") String id, @Param("status") String status) {
        StringBuilder sb = new StringBuilder();
        sb.append("update tb_file set status = #{status} where id = #{id}");
        return sb.toString();
    }

    public String listFileInfos(@Param("ids") List<String> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id, suffix, original_name, status, create_time, dir_path, creator_id, creator_name");
        sb.append(" from tb_file");
        sb.append(" where id in <foreach item='id' collection='ids' open='(' separator=',' close=')'>#{id}</foreach>");
        return sb.toString();
    }

    public String getFileInfo(@Param("id") String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id, suffix, original_name, status, create_time, dir_path, creator_id, creator_name");
        sb.append(" from tb_file where id = #{id}");
        return sb.toString();
    }

    public String removeFiles(@Param("ids") List<String> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from tb_file");
        sb.append(" where id in <foreach item='id' collection='ids' open='(' separator=',' close=')'>#{id}</foreach>");
        return sb.toString();
    }
}

package com.augurit.tb.mapper.sql;

import com.augurit.tb.entity.TbTask;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;

public class TbTaskMapperSQL {

    public String listTasks(@Param("name") String name, @Param("status") String status){
        StringBuilder sb = new StringBuilder();
        sb.append("select id, name taskName, create_time, creator_id, creator_name from tb_task");
        sb.append(" where status = #{status}");
        if(!Strings.isBlank(name)) {
            sb.append(" and name like concat('%', #{name}, '%')");
        }
        return sb.toString();
    }

    public String saveTask(TbTask task) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_task(id, name, create_time, creator_id, creator_name) values(#{id}, #{taskName}, now(), #{creatorId}, #{creatorName})");
        return sb.toString();
    }

    public String updateTask(TbTask task) {
        StringBuilder sb = new StringBuilder();
        sb.append("update tb_task set status = #{status}");
        sb.append(" where id = #{id}");
        if(task.getCreatorId() != null) {
            sb.append(" and creator_id = #{creatorId}");
        }
        return sb.toString();
    }
}

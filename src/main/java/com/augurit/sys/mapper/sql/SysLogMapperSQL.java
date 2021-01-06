package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysLog;
import org.apache.commons.lang.StringUtils;

public class SysLogMapperSQL {

    public String list(SysLog sysLog){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_log where 1=1");
        if(StringUtils.isNotBlank(sysLog.getIp())){
            sb.append(" and ip like concat('%',#{ip},'%')");
        }
        if(StringUtils.isNotBlank(sysLog.getUsername())){
            sb.append(" and username like concat('%',#{username},'%')");
        }
        if(StringUtils.isNotBlank(sysLog.getNickname())){
            sb.append(" and nickname like concat('%',#{nickname},'%')");
        }
        if(StringUtils.isNotBlank(sysLog.getOperation())){
            sb.append(" and operation like concat('%',#{operation},'%')");
        }
        sb.append(" order by create_date desc");
        return sb.toString();
    }
}

package com.augurit.sys.mapper.sql;


import com.augurit.sys.entity.SysFunction;

public class SysFunctionMapperSQL {
    public String list(SysFunction sysFunction){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_function where 1=1");
        if(sysFunction.getUniqueCode()!=null){
            sb.append(" and unique_code like concat('%',#{uniqueCode},'%')");
        }

        if(sysFunction.getName()!=null){
            sb.append(" and name like concat('%',#{name},'%')");
        }

        return sb.toString();
    }

}

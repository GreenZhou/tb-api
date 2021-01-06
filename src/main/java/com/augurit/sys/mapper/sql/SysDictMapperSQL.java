package com.augurit.sys.mapper.sql;

import com.augurit.sys.entity.SysDict;

public class SysDictMapperSQL {

    public String list(SysDict sysDict){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from sys_dict where 1=1 ");
        if(sysDict.getDictId()!=null){
            sb.append(" and dict_id=#{dictId}");
        }
        if(sysDict.getParentId()!=null){
            sb.append(" and parent_id=#{parentId}");
        }
        if(sysDict.getCode()!=null){
            sb.append(" and (code like concat('%',#{code},'%') or name like concat('%',#{code},'%'))");
        }
        return sb.toString();
    }

    public String deleteByParentId(Long parentId){
        StringBuilder sb = new StringBuilder();
        sb.append("delete from sys_dict where parent_id=#{parentId}");
        return sb.toString();
    }
}

package com.augurit.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "结果集")
public class QueryResult<T> {
    @ApiModelProperty(value = "信息")
    private String msg;
    @ApiModelProperty(value = "状态码(0:失败,1:成功)", allowableValues = "0,1")
    private int code;

    public PageResult<T> data=new PageResult<T>();

    public static QueryResult success() {
        return success("成功");
    }

    public static QueryResult success(String msg) {
        QueryResult result = new QueryResult();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }

    public static QueryResult error() {
        QueryResult result = new QueryResult();
        result.setCode(0);
        result.setMsg("请求失败,请联系管理员");
        return result;
    }

    public static QueryResult error(String msg) {
        QueryResult result =error();
        result.setMsg(msg);
        return result;
    }

    public QueryResult addPage(PageResult data) {
        setData(data);
        return this;
    }

    public QueryResult addEntity(T obj){
        List<T> list=new ArrayList<T>();
        if(obj!=null)
        list.add(obj);
        data.setList(list);
        return this;
    }

    public QueryResult addList(List<T> list){
        data.setList(list);
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PageResult getData() {
        return data;
    }

    public void setData(PageResult data) {
        this.data = data;
    }

}



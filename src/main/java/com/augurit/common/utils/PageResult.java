package com.augurit.common.utils;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "分页结果类")
public class PageResult<T> {

    public PageResult(){

    }

    public PageResult(List<T> list){
        if(list instanceof Page){
            Page page = (Page)list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total=page.getTotal();
        }
        this.list=list;
    }

    @ApiModelProperty(value = "当前页码",allowableValues = "range[1,10]")
    private int pageNum;
    @ApiModelProperty(value = "每页显示记录数",allowableValues = "10,20,30")
    private int pageSize;
    @ApiModelProperty(value = "记录总数",allowableValues = "range[0,1000]")
    private long total;
    @ApiModelProperty(value = "记录详情")
    private List<T> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
package com.augurit.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

@ApiModel(value = "分页查询参数")
public class QueryParam {
    @ApiModelProperty(value = "当前页码",allowableValues = "range[1,10]")
    private int pageNum;
    @ApiModelProperty(value = "每页显示记录数",allowableValues = "10,20,30")
    private int pageSize;
    @ApiModelProperty(value = "排序语句",allowableValues = "id desc,name asc")
    private String orderBy;

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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}

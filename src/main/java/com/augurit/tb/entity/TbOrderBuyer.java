package com.augurit.tb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "订单-买家统计表")
@Data
public class TbOrderBuyer {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "子任务ID")
    private String tsid;

    @ApiModelProperty(value = "商家名称")
    private String salerName;

    @ApiModelProperty(value = "买家ID")
    private String buyerId;

    @ApiModelProperty(value = "买家旺旺")
    private String buyerWWName;

    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "应付数量")
    private Integer num;

    @ApiModelProperty(value = "应付单价")
    private Double price;

    @ApiModelProperty(value = "应付合计")
    private Double tprice;

    @ApiModelProperty(value = "实付数量")
    private Integer numSj;

    @ApiModelProperty(value = "实付单价")
    private Double priceSj;

    @ApiModelProperty(value = "实付合计")
    private Double tpriceSj;

    @ApiModelProperty(value = "买家佣金")
    private Double byj;

    @ApiModelProperty(value = "核查状态")
    private String checkStatus;

    @ApiModelProperty(value = "下单状态")
    private String xdStatus;
}

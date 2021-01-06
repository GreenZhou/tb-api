package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "订单")
@Data
@TableName("tb_order")
public class TbOrder implements Cloneable {
    @ApiModelProperty(value = "订单ID")
    private String id;

    @ApiModelProperty(value = "子任务ID")
    private String tsid;

    @ApiModelProperty(value = "子任务名称")
    @TableField(exist=false)
    private String subTaskName;

    @ApiModelProperty(value = "商家名称")
    private String salerName;

    @ApiModelProperty(value = "商家旺旺")
    private String salerWWName;

    @ApiModelProperty(value = "单价")
    private Double price;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "图片地址")
    private String purl;

    @ApiModelProperty(value = "佣金")
    private Double yj;

    @ApiModelProperty(value = "关键字")
    private String keywd;

    @ApiModelProperty(value = "要求")
    private String demand;

    @ApiModelProperty(value = "总价")
    private Double tprice;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "买家旺旺")
    private String buyerWwName;

    @ApiModelProperty(value = "买家佣金")
    private Double byj;

    @Override
    public TbOrder clone() {
        try {
            return (TbOrder) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }
}

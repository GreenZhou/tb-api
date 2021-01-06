package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "买家")
@Data
@TableName("tb_buyer")
public class TbBuyer {
    @ApiModelProperty(value = "买家ID")
    private String id;

    @ApiModelProperty(value = "子任务ID")
    private String tsid;

    @ApiModelProperty(value = "买家旺旺")
    private String buyerWWName;

    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;

    @ApiModelProperty(value = "买家佣金")
    private Double byj;

    @ApiModelProperty(value = "状态")
    private String status;
}

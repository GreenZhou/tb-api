package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "任务")
@Data
@TableName("tb_task")
public class TbTask {
    @ApiModelProperty(value = "任务ID")
    private String id;

    @ApiModelProperty(value = "任务名")
    private String taskName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人ID")
    private Long creatorId;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建人")
    private String status;
}

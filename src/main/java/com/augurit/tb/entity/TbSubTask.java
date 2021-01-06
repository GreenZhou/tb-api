package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "子任务")
@Data
@TableName("tb_task_sub")
public class TbSubTask {

    @ApiModelProperty(value = "子任务ID")
    private String id;

    @ApiModelProperty(value = "任务ID")
    private String pid;

    @ApiModelProperty(value = "子任务名")
    private String subTaskName;

    @ApiModelProperty(value = "订单总数")
    private Integer orderNum;

    @ApiModelProperty(value = "需要人员数量")
    private Integer buyerNeedNum;

    @ApiModelProperty(value = "已上传人员数量")
    @TableField(exist=false)
    private Long uploadedBuyerNum;

    @ApiModelProperty(value = "实际人员数量")
    private Integer buyerNum;

    @ApiModelProperty(value = "分配员工列表")
    @TableField(exist=false)
    private List<TbSubTaskEmp> emps;

}

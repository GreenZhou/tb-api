package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "子任务员工分配")
@Data
@TableName("tb_task_sub_emp")
public class TbSubTaskEmp {
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "子任务ID")
    private String tsid;

    @ApiModelProperty(value = "员工ID")
    private Long empId;

    @ApiModelProperty(value = "员工名称")
    private String empName;

    @ApiModelProperty(value = "核查状态")
    private String checkStatus;
}

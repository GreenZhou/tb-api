package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "功能")
@Data
@TableName("sys_function")
@KeySequence("sys_function_function_id_seq")
public class SysFunction implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "功能ID",allowableValues = "range[1,10000]")
    @TableId(type = IdType.AUTO)
    private Long functionId;

    @ApiModelProperty(value = "功能名称")
    private String name;

    @ApiModelProperty(value = "功能编码")
    private String uniqueCode;

    @ApiModelProperty(value = "状态(0:未激活;1:激活)",allowableValues = "0,1")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}

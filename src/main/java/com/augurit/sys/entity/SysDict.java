package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="字典")
@Data
@TableName("sys_dict")
@KeySequence("sys_dict_dict_id_seq")
public class SysDict {

    @ApiModelProperty(value = "字典ID",allowableValues = "range[1,10]")
    @TableId(type = IdType.AUTO)
    private Long dictId;

    @ApiModelProperty(value = "上级字典ID，顶级字典0",allowableValues = "range[0,10]")
    private Long parentId;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典编码")
    private String code;

    @ApiModelProperty(value = "排序",allowableValues = "range[0,10]")
    private Integer orderNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态0:禁用;1:正常",allowableValues = "0,1")
    private Integer status;

}

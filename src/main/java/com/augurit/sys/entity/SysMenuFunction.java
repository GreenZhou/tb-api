package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单与功能对应关系
 */
@Data
@TableName("sys_menu_function")
@KeySequence("sys_menu_function_id_seq")
public class SysMenuFunction implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @ApiModelProperty(value = "菜单功能ID",allowableValues = "range[1,10000]")
	@TableId(type = IdType.AUTO)
	private Long id;

    @ApiModelProperty(value = "菜单ID",allowableValues = "range[1,10000]")
	private Long menuId;

    @ApiModelProperty(value = "功能ID",allowableValues = "range[1,10000]")
	private Long functionId;

    @ApiModelProperty(value = "菜单功能状态(0:未激活;1:激活)",allowableValues = "0,1")
    @TableField(exist=false)
    private Integer mfStatus;

    @ApiModelProperty(value = "功能状态(0:未激活;1:激活)",allowableValues = "0,1")
    @TableField(exist=false)
    private Integer status;

    @ApiModelProperty(value = "功能名称")
    @TableField(exist=false)
    private String name;

    @ApiModelProperty(value = "功能编码")
    @TableField(exist=false)
    private String uniqueCode;

    @ApiModelProperty(value = "备注")
    @TableField(exist=false)
    private String remark;
}

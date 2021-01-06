package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "菜单")
@Data
@TableName("sys_menu")
@KeySequence("sys_menu_menu_id_seq")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单ID",allowableValues = "range[1,10000]")
    @TableId(type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty(value = "父菜单ID，一级菜单为0",allowableValues = "range[1,10000]")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单URL")
    private String url;

    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)",allowableValues = "user:list,user:create")
    private String perms;

    @ApiModelProperty(value = "类型(0:目录;1:菜单;2:按钮)",allowableValues = "0,1,2")
    private Integer type;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "排序",allowableValues="range[1,10]")
    private Integer orderNum;

    @ApiModelProperty(value = "状态(0:未激活;1:激活)",allowableValues = "0,1")
    private Integer status;

    @ApiModelProperty(value = "唯一码")
    private String uniqueCode;

    @ApiModelProperty(value = "角色ID(多个用逗号分隔)")
    @TableField(exist=false)
    private String roles;

    @ApiModelProperty(value = "功能ID(多个用逗号分隔)")
    @TableField(exist=false)
    private String funcIds;

    @ApiModelProperty(value = "功能名称(多个用逗号分隔)")
    @TableField(exist=false)
    private String funcNames;

    @ApiModelProperty(value = "功能编码(多个用逗号分隔)")
    @TableField(exist=false)
    private String funcUniqueCodes;

    @ApiModelProperty(value = "更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
    
    @ApiModelProperty(value = "ztree属性")
    @TableField(exist=false)
    private List<?> list;
}

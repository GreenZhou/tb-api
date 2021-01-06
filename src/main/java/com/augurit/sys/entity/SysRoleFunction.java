package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与功能对应关系
 */
@Data
@TableName("sys_role_function")
@KeySequence("sys_role_function_id_seq")
public class SysRoleFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色功能ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "功能ID")
    private Long functionId;
}

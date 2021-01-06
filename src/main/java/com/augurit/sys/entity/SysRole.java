package com.augurit.sys.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 */
@ApiModel(value = "角色")
@Data
@TableName("sys_role")
@KeySequence("sys_role_role_id_seq")
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "角色ID",allowableValues = "range[1,10]")
	@TableId(type = IdType.AUTO)
	private Long roleId;

	@ApiModelProperty(value = "角色名称")
	private String roleName;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "父角色ID",allowableValues = "range[1,10]")
	private Long parentId;

	@ApiModelProperty(value = "机构ID",allowableValues = "range[1,10]")
	private Long deptId;

	@ApiModelProperty(value = "机构名称")
	@TableField(exist=false)
	private String deptName;

	@ApiModelProperty(value = "人员列表")
	@TableField(exist=false)
	private List<Long> userIdList;

	@ApiModelProperty(value = "菜单列表")
	@TableField(exist=false)
	private List<Long> menuIdList;

	@ApiModelProperty(value = "机构列表")
	@TableField(exist=false)
	private List<Long> deptIdList;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
}

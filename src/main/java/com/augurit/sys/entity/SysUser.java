package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "用户")
@Data
@TableName("sys_user")
@KeySequence("sys_user_user_id_seq")
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户ID",allowableValues = "range[1,100]")
	@TableId(type = IdType.AUTO)
	private Long userId;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "密码")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@ApiModelProperty(value = "角色ID")
	private String salt;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "手机号")
	private String mobile;

	@ApiModelProperty(value = "状态  0：禁用   1：正常",allowableValues = "0,1")
	private Integer status;

	@ApiModelProperty(value = " 锁定  0：被锁   1：正常",allowableValues = "0,1")
	private Integer locked;

	@ApiModelProperty(value = "被禁次数",allowableValues = "range[1,3]")
	private Integer lockedNum;

	@ApiModelProperty(value = "最后一次登录错误时间")
	private Date errorTime;

	@ApiModelProperty(value = "职务")
	private String job;

	@ApiModelProperty(value = "办公电话")
	private Long phone;

	@ApiModelProperty(value = "分机号")
	private Long exnumber;

	@ApiModelProperty(value = "角色列表")
	@TableField(exist=false)
	private List<Long> roleIdList;

	@ApiModelProperty(value = "机构列表")
	@TableField(exist=false)
	private List<Long> deptIdList;

	@ApiModelProperty(value = "所有子机构列表")
	@TableField(exist=false)
	private List<Long> subDeptIdList;

	@ApiModelProperty(value = "机构id(多个用逗号分隔)")
	@TableField(exist=false)
	private String deptIds;

	@ApiModelProperty(value = "机构名称(多个用逗号分隔)")
	@TableField(exist=false)
	private String deptNames;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "昵称")
	private String nickname;

	@ApiModelProperty(value = "机构目录")
	@TableField(exist=false)
	private String deptDirectory;

	@ApiModelProperty(value = "角色开关",allowableValues = "true,false")
	@TableField(exist=false)
	private Boolean open=false;

	/**
	 * 有效期开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startTime;
	/**
	 * 有效期结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;

	@ApiModelProperty(value = "排序")
	private Long orderNum;
}

package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *
 */
@ApiModel(value = "日志")
@Data
@TableName("sys_log")
@KeySequence("sys_log_id_seq")
public class SysLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "日志ID",allowableValues = "range[1,10]")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "用户昵称")
	private String nickname;

	@ApiModelProperty(value = "用户操作")
	private String operation;

	@ApiModelProperty(value = "请求方法")
	private String method;

	@ApiModelProperty(value = "请求参数")
	private String params;

	@ApiModelProperty(value = "执行时长",allowableValues = "range[1000,10000]")
	private Long time;

	@ApiModelProperty(value = "IP地址",allowableValues = "192.168.1.1,192.168.1.2")
	private String ip;

	@ApiModelProperty(value = "结果(0:失败;1:成功)",allowableValues = "0,1")
	private Integer result;

	@ApiModelProperty(value = "创建时间",allowableValues = "192.168.1.1,192.168.1.2")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDate;

}

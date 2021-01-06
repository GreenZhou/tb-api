package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "机构")
@Data
@TableName("sys_dept")
@KeySequence("sys_dept_dept_id_seq")
public class SysDept implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "机构ID",allowableValues = "range[0,100]")
	@TableId(type = IdType.AUTO)
	private Long deptId;

	@ApiModelProperty(value = "上级机构ID，一级机构为0",allowableValues = "range[0,100]")
	private Long parentId;

	@ApiModelProperty(value = "机构名称")
	private String name;

	@ApiModelProperty(value = "上级机构名称")
	@TableField(exist=false)
	private String parentName;

	@ApiModelProperty(value = "排序顺序",allowableValues ="range[0,100]")
	private Integer orderNum=0;

//	@TableLogic
//	@ApiModelProperty(value = "删除标识(0:没有删除,1:已删除)",allowableValues="0,1")
//	private Integer delFlag;

	@ApiModelProperty(value = "显示机构下的人数",allowableValues ="range[0,100]")
	@TableField(exist=false)
	private Long count;

	@ApiModelProperty(value = "ztree属性(是否打开)",allowableValues = "true,false")
	@TableField(exist=false)
	private Boolean open;

	@ApiModelProperty(value = "下级机构")
	@TableField(exist=false)
	private List<?> list;

	@ApiModelProperty(value = "是否有用户直属于该机构")
	@TableField(exist=false)
	private Boolean existUser;

}

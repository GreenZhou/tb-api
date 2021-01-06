package com.augurit.sys.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与机构对应关系
 */
@Data
@TableName("sys_role_dept")
@KeySequence("sys_role_dept_id_seq")
public class SysRoleDept implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 机构ID
	 */
	private Long deptId;

	
}

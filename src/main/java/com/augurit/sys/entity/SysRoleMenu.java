package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 */
@Data
@TableName("sys_role_menu")
@KeySequence("sys_role_menu_id_seq")
public class SysRoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单ID
	 */
	private Long menuId;

	
}

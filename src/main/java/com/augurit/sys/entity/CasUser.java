package com.augurit.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 单点登录用户类
 */
@Data
public class CasUser {
    public CasUser(String userName, String password, String captcha) {
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    @JsonProperty("@class")
    private String clazz = "org.apereo.cas.authentication.principal.SimplePrincipal";
    @JsonProperty("id")
    private String userName;
    private String nickname;
    /**
     * 用户ID
     */
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String captcha;
    /**
     * 锁定  0：被禁   1：正常
     */
    @JsonIgnore
    private Integer status;
    /**
     * 锁定  0：被锁   1：正常
     */
    @JsonIgnore
    private Integer locked;
    /**
     * 被禁次数
     */
    @JsonIgnore
    private Integer lockedNum;
    /**
     * 最后一次登录错误时间
     */
    @JsonIgnore
    private Date errorTime;

    @JsonIgnore
    private String salt;

    @ApiModelProperty(value = "菜单列表")
    @TableField(exist=false)
    private List<SysMenu> menuList;

    @ApiModelProperty(value = "角色列表")
    @TableField(exist=false)
    private List<SysRole> roleList;

    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    /**
     * 有效期开始时间
     */
    @JsonIgnore
    private Date startTime;
    /**
     * 有效期结束时间
     */
    @JsonIgnore
    private Date endTime;
}

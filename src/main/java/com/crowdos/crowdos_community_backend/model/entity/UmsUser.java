package com.crowdos.crowdos_community_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@TableName("ums_user")
@NoArgsConstructor
@AllArgsConstructor
public class UmsUser implements Serializable {
    private static final long serialVersionUID = -5051120337175047163L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 用户昵称
     */
    @TableField("alias")
    private String alias;
    /**
     * 密码
     */
    @JsonIgnore
    @TableField("password")
    private String password;
    /**
     * 头像
     */
    @Builder.Default
    @TableField("avatar")
    private String avatar = "https://github.com/li-haoyang1/CrowdOS_community/blob/main/src/picture/CrowdOS.jpg";;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;
    /**
     * 积分
     */
    @TableField("score")
    private Integer score;
    /**
     * token
     */
    @TableField("token")
    private String token;
    /**
     * 个人简介
     */
    @Builder.Default
    @TableField("bio")
    private String bio = "自由职业者";
    /**
     * 是否激活
     */
    @Builder.Default
    @TableField("active")
    private Boolean active = true;
    /**
     * 状态，1：使用，0：停用
     */
    @Builder.Default
    @TableField("status")
    private Boolean status = true;
    /**
     * 用户角色
     */
    @TableField("role_id")
    private Integer roleId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "modify_time", fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
package com.crowdos.crowdos_community_backend.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bms_follow")
public class BmsFollow implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增添加
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 被关注者id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 粉丝id
     */
    @TableField("follower_id")
    private String followerId;

    public BmsFollow() {

    }
}

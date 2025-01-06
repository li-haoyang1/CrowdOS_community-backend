package com.crowdos.crowdos_community_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@TableName("bms_promotion")
@NoArgsConstructor
@AllArgsConstructor
public class BmsPromotion implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 链接名标题
     */
    @TableField("title")
    private String title;
    /**
     * 广告连接
     */
    @TableField("link")
    private String link;
    /**
     * 链接描述
     */
    @TableField("description")
    private String description;
}
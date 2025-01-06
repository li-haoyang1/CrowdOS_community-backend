package com.crowdos.crowdos_community_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@TableName("bms_post")
@NoArgsConstructor
@AllArgsConstructor
public class BmsPost implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 帖子标题
     */
    @NotEmpty(message = "标题不仅可以为空")
    @TableField("title")
    private String title;
    /**
     * 帖子内容
     */
    @NotEmpty(message = "内容不可以为空")
    @TableField("content")
    private String content;
    /**
     * 作者id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 评论数
     */
    @TableField("comments")
    @Builder.Default
    private Integer comments = 0;
    /**
     * 收藏数
     */
    @TableField("collects")
    @Builder.Default
    private Integer collects = 0;
    /**
     * 浏览数
     */
    @TableField("view")
    @Builder.Default
    private Integer view = 0;
    /**
     * 是否置顶，默认不置顶
     */
    @TableField("top")
    @Builder.Default
    private Boolean top = false;
    /**
     * 专栏ID，默认不分栏
     */
    @TableField("section_id")
    @Builder.Default
    private Integer sectionId = 0;
    /**
     * 加精
     */
    @TableField("essence")
    @Builder.Default
    private Boolean essence = false;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}
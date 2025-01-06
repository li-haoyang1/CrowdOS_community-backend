package com.crowdos.crowdos_community_backend.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("bms_comment")
public class BmsComment {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 评论内容
     */
    @NotEmpty(message = "内容不能为空")
    @TableField("content")
    private String content;
    /**
     * 作者ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 被评论的帖子id
     */
    @TableField("topic_id")
    private String topicId;
    /**
     *创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}

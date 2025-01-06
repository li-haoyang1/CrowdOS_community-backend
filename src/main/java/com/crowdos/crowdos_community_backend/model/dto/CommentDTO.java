package com.crowdos.crowdos_community_backend.model.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;
    /**
     * 内容
     */
    @NotEmpty(message = "评论内容不能为空")
    private String content;
    /**
     * 帖子id
     */
    private String topic_id;
}

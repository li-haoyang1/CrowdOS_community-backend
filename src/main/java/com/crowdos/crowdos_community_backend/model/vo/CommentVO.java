package com.crowdos.crowdos_community_backend.model.vo;


import lombok.Data;

@Data
public class CommentVO {
    /**
     * 评论id
     */
    private String id;
    /**
     * 1
     */
    private String content;
    /**
     * ·
     */
    private String topicId;
    /**
     * 1
     */
    private String userId;
    /**
     * 本VO最主要的作用，给出userName
     */
    private String userName;
    /**
     * 1
     */
    private String createTime;
}

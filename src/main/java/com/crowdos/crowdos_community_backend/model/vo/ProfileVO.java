package com.crowdos.crowdos_community_backend.model.vo;


import lombok.Data;

@Data
public class ProfileVO {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称（别称）
     */
    private String alias;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户关注数
     */
    private Integer followCount;
    /**
     * 用户被关注数（粉丝数）
     */
    private Integer followerCount;
    /**
     * 用户文章数
     */
    private Integer topicCount;
    /**
     * 用户专栏数
     */
    private Integer columns;
    /**
     * 评论数
     */
    private Integer commentCount;
}

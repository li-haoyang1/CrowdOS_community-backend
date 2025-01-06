package com.crowdos.crowdos_community_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;

import java.util.List;

public interface IBmsTagService extends IService<BmsTag> {
    /**
     * 插入标签
     *
     */
    List<BmsTag> insertTags(List<String> tags);
    /**
     * 获取标签的关联话题
     */
    Page<BmsPost> selectTopicsById(Page<BmsPost> topicPage, String id);
}

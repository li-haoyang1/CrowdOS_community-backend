package com.crowdos.crowdos_community_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;
import com.crowdos.crowdos_community_backend.model.entity.BmsTopicTag;

import java.util.List;
import java.util.Set;

public interface IBmsTopicTagService extends IService<BmsTopicTag> {
    /**
     * 获取Topic Tag 关联记录
     *
     * @param topicId TopicId
     * @return
     */
    List<BmsTopicTag> selectByTopicId(String topicId);
    /**
     * 创建中间关系
     */
    void createTopicTag(String id, List<BmsTag> tags);

    Set<String> selectTopicIdByTagId(String id);
}

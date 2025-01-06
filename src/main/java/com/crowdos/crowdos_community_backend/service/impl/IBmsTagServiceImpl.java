package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsTagMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;
import com.crowdos.crowdos_community_backend.service.IBmsPostService;
import com.crowdos.crowdos_community_backend.service.IBmsTagService;
import com.crowdos.crowdos_community_backend.service.IBmsTopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Tag实现类
 *
 * @author lhy
 */
@Service
public class IBmsTagServiceImpl extends ServiceImpl<BmsTagMapper, BmsTag> implements IBmsTagService {
    @Autowired
    private com.crowdos.crowdos_community_backend.service.IBmsTopicTagService iBmsTopicTagService;

    @Autowired
    private com.crowdos.crowdos_community_backend.service.IBmsPostService iBmsPostService;

    @Override
    public List<BmsTag> insertTags(List<String> tagNames) {
        List<BmsTag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            BmsTag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsTag>().eq(BmsTag::getName, tagName));
            if (tag == null) {
                tag = BmsTag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public Page<BmsPost> selectTopicsById(Page<BmsPost> topicPage, String id){
        //获取关联的话题id
        Set<String> ids = iBmsTopicTagService.selectTopicIdByTagId(id);
        LambdaQueryWrapper<BmsPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BmsPost::getId, ids);
        return iBmsPostService.page(topicPage, wrapper);
    }
}

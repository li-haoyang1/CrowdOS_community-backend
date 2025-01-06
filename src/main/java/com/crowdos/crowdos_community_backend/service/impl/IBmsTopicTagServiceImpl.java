package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsTopicTagMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;
import com.crowdos.crowdos_community_backend.model.entity.BmsTopicTag;
import com.crowdos.crowdos_community_backend.service.IBmsTopicTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class IBmsTopicTagServiceImpl extends ServiceImpl<BmsTopicTagMapper, BmsTopicTag> implements IBmsTopicTagService {
    @Override
    //MyBatis-Plus 提供的 QueryWrapper 和 Lambda 表达式功能，实现动态 SQL 查询。
    public List<BmsTopicTag> selectByTopicId(String topicId) {
        //创建一个MyBatis-Plus的查询条件包装器QueryWrapper，BmsTopicTag指定了操作目标是BmsTopicTag实体类
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        //通过调用 lambda()，将字段表达式转化为 Java 的 Lambda 语法，作用是生成 SQL 条件 WHERE topic_id = #{topicId}
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topicId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void createTopicTag(String id, List<BmsTag> tags){
        //先删除topicID对应的记录
        this.baseMapper.delete(new LambdaQueryWrapper<BmsTopicTag>().eq(BmsTopicTag::getTopicId, id));

        //循环保存对应关联
        tags.forEach(tag -> {
            BmsTopicTag bmsTopicTag = new BmsTopicTag();
            bmsTopicTag.setTopicId(id);
            bmsTopicTag.setTagId(tag.getId());
            this.baseMapper.insert(bmsTopicTag);
        });
    }

    @Override
    public Set<String> selectTopicIdByTagId(String id){
        return this.baseMapper.getTopicIdByTagId(id);
    }
}

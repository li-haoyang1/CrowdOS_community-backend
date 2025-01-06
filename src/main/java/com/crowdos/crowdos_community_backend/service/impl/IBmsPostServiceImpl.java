package com.crowdos.crowdos_community_backend.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsTagMapper;
import com.crowdos.crowdos_community_backend.mapper.BmsTopicMapper;
import com.crowdos.crowdos_community_backend.mapper.UmsUserMapper;
import com.crowdos.crowdos_community_backend.model.dto.CreateTopicDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;
import com.crowdos.crowdos_community_backend.model.entity.BmsTopicTag;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.PostVO;
import com.crowdos.crowdos_community_backend.model.vo.ProfileVO;
import com.crowdos.crowdos_community_backend.service.IBmsPostService;
import com.crowdos.crowdos_community_backend.service.IBmsTagService;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IBmsPostServiceImpl extends ServiceImpl<BmsTopicMapper, BmsPost> implements IBmsPostService {
    @Resource
    private BmsTagMapper bmsTagMapper;

    @Resource
    private UmsUserMapper umsUserMapper;

    @Autowired
    @Lazy
    private IBmsTagService ibmsTagService;

    @Autowired
    private IUmsUserService iUmsUserService;

    @Autowired
    private com.crowdos.crowdos_community_backend.service.IBmsTopicTagService IBmsTopicTagService;

    /**
     * @param page
     * @param tab
     * @return
     */
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        //查询话题
        Page<PostVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        //通过标签查询
        iPage.getRecords().forEach(topic -> {
            List<BmsTopicTag> topicTags = IBmsTopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream()
                        .map(BmsTopicTag::getTagId)
                        .collect(Collectors.toList());
                List<BmsTag> tags = bmsTagMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
        return iPage;
    }

    /**
     * 添加帖子
     *
     * @param dto
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BmsPost create(CreateTopicDTO dto, UmsUser user) {
        BmsPost topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getTitle, dto.getTitle()));
        Assert.isNull(topic1, "话题已存在，请修改");

        //封装
        BmsPost topic = BmsPost.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createTime(new Date())
                .build();
        this.baseMapper.insert(topic);

        //用户积分增加
        int newScore = user.getScore() + 1;
        umsUserMapper.updateById(user.setScore(newScore));

        //标签
        if (!ObjectUtils.isEmpty(dto.getTags())) {
            //保存标签
            List<BmsTag> tags = ibmsTagService.insertTags(dto.getTags());
            //处理标签于话题的关联
            IBmsTopicTagService.createTopicTag(topic.getId(), tags);
        }

        return topic;
    }

    /**
     * @param id
     * @return {@link Map }<{@link String }, {@link Object }>
     */
    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        BmsPost topic = this.baseMapper.selectById(id);
        //Assert 是 Spring Framework 提供的一个工具类，位于 org.springframework.util.Assert 包中，
        // 主要用于在开发过程中进行断言检查，验证某些条件是否满足。如果条件不满足，通常会抛出一个 IllegalArgumentException 或其他运行时异常
        Assert.notNull(topic, "当前话题不存在，或已被作者删除");
        //查询话题详情
        topic.setView(topic.getView() + 1);
        this.baseMapper.updateById(topic);
        //emoji转码
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        map.put("topic", topic);
        //标签
        //MyBatis-Plus 中用于构建查询条件的对象，QueryWrapper 是一个查询条件构造器，可以用来方便地生成动态 SQL 查询语句
        //lambda() 方法是 QueryWrapper 的一种扩展，用于获取一个 LambdaQueryWrapper 对象,
        //LambdaQueryWrapper 使用 Lambda 表达式，让查询条件绑定到实体类的字段，从而避免了直接使用字符串字段名可能导致的拼写错误
        QueryWrapper<BmsTopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmsTopicTag::getTopicId, topic.getId());
        Set<String> set = new HashSet<>();
        for (BmsTopicTag articleTag : IBmsTopicTagService.list(wrapper)) {
            set.add(articleTag.getTagId());
        }
        List<BmsTag> tags = ibmsTagService.listByIds(set);
        map.put("tags", tags);

        //作者

        ProfileVO user = iUmsUserService.getUserProfile(topic.getUserId());
        map.put("user", user);

        return map;
    }

    /**
     * 获取推荐列表
     *
     * @param id
     * @return
     */
    @Override
    public List<BmsPost> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }


    /**
     * 通过关键字搜索
     *
     * @param keyword
     * @param page
     * @return
     */
    @Override
    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);

        // 查询话题的标签
        iPage.getRecords().forEach(topic -> {
            List<BmsTopicTag> topicTags = IBmsTopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream().map(BmsTopicTag::getTagId).collect(Collectors.toList());
                List<BmsTag> tags = bmsTagMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
        return iPage;
    }
}


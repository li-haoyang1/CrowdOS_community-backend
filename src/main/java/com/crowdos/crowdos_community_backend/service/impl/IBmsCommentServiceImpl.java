package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsCommentMapper;
import com.crowdos.crowdos_community_backend.model.dto.CommentDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsComment;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.CommentVO;
import com.crowdos.crowdos_community_backend.service.IBmsCommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class IBmsCommentServiceImpl extends ServiceImpl<BmsCommentMapper, BmsComment> implements IBmsCommentService {

    @Override
    public List<CommentVO> getCommentsByTopicID(String topicid) {
        List<CommentVO> listComment = new ArrayList<CommentVO>();
        try {
            listComment = this.baseMapper.getCommentsByTopicID(topicid);
        } catch (Exception e) {
            log.info("listComment失败");
        }
        return listComment;
    }

    @Override
    public BmsComment addComment(CommentDTO commentDTO, UmsUser user) {
        BmsComment comment = BmsComment.builder()
                .content(commentDTO.getContent())
                .userId(user.getId())
                .topicId(commentDTO.getTopic_id())
                .createTime(new Date())
                .build();
        this.baseMapper.insert(comment);
        return comment;
    }
}

package com.crowdos.crowdos_community_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsComment;
import com.crowdos.crowdos_community_backend.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BmsCommentMapper extends BaseMapper<BmsComment> {
    /**
     * 通过帖子ID得到评论
     *
     * @param topicid
     * @return
     */
    List<CommentVO> getCommentsByTopicID(@Param("topicid") String topicid);
}

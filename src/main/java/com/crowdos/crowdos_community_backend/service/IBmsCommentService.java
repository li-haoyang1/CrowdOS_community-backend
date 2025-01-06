package com.crowdos.crowdos_community_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowdos.crowdos_community_backend.model.dto.CommentDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsComment;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.CommentVO;

import java.util.List;

public interface IBmsCommentService extends IService<BmsComment> {
    List<CommentVO> getCommentsByTopicID(String topicid);

    /**
     *
     * @param commentDTO
     * @param user
     * @return
     */
    BmsComment addComment(CommentDTO commentDTO, UmsUser user);
}

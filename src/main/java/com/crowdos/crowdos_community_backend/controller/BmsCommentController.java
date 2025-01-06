package com.crowdos.crowdos_community_backend.controller;


import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.model.dto.CommentDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsComment;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.CommentVO;
import com.crowdos.crowdos_community_backend.service.IBmsCommentService;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.crowdos.crowdos_community_backend.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/comment")
public class BmsCommentController extends BaseController{

    //在 Spring 框架中，使用 接口（Interface） 而不是直接使用实现类（Impl）是一种良好的设计和开发实践，主要是为了提高代码的灵活性、扩展性和可维护性。
    //
    @Resource
    private IBmsCommentService bmsCommentService;

    @Resource
    private IUmsUserService umsUserService;

    /**
     * 得到评论信息
     *
     * @param topicid
     * @return
     */
    @GetMapping("/get_comments")
    public ApiResult<List<CommentVO>> getCommentsByTopicID(@RequestParam(value = "topicid", defaultValue = "1") String topicid) {
        List<CommentVO> commentVOList = bmsCommentService.getCommentsByTopicID(topicid);
        return ApiResult.success(commentVOList);
    }

    @RequestMapping(value = "/add_comment", method = RequestMethod.POST)
    public ApiResult<BmsComment> addComment(@RequestHeader(value = USER_NAME) String userName,
                                            @RequestBody CommentDTO commentDTO) {
        UmsUser user = umsUserService.getUserByUsername(userName);
        BmsComment comment = bmsCommentService.addComment(commentDTO, user);
        return ApiResult.success(comment);
    }
}

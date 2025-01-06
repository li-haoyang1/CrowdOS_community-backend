package com.crowdos.crowdos_community_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.common.exception.ApiAsserts;
import com.crowdos.crowdos_community_backend.model.entity.BmsFollow;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.service.IBmsFollowService;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static com.crowdos.crowdos_community_backend.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/relationship")
public class BmsRelationshipController extends BaseController {

    @Resource
    private IBmsFollowService bmsFollowService;

    @Resource
    private IUmsUserService umsUserService;

    /**
     *关注用户的功能接口
     *
     * @param userName
     * @param parentId
     * @return
     */
    @GetMapping("/subscribe/{userId}")
    public ApiResult<Object> handleFollow(@RequestHeader(value = USER_NAME) String userName,
                                          @PathVariable("userId") String parentId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        if(parentId.equals(umsUser.getId())) {
            ApiAsserts.fail("不可以关注自己哦！🙂");
        }
        BmsFollow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<BmsFollow>()
                        .eq(BmsFollow::getParentId, parentId)
                        .eq(BmsFollow::getFollowerId, umsUser.getId()));
        if(!ObjectUtils.isEmpty(one)) {
            ApiAsserts.fail("已关注");
        }
        BmsFollow follow = new BmsFollow();
        follow.setFollowerId(umsUser.getId());
        follow.setParentId(parentId);
        bmsFollowService.save(follow);
        return ApiResult.success("关注成功！");
    }

    /**
     * 取消关注用户功能的接口
     * @param userName
     * @param parentId
     * @return
     */
    @GetMapping("/unsubscribe/{userId}")
    public ApiResult<Object> handleUnFollow(@RequestHeader(value = USER_NAME) String userName,
                                            @PathVariable("userId") String parentId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        //获得关注表中对应的关注信息
        BmsFollow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<BmsFollow>()
                        .eq(BmsFollow::getParentId, parentId)
                        .eq(BmsFollow::getFollowerId, umsUser.getId()));
        if(ObjectUtils.isEmpty(one)) {
            ApiAsserts.fail("未关注！");
        }
        //removeById需要传入主键
        bmsFollowService.removeById(one.getId());
        return ApiResult.success("取关成功！");
    }

    /**
     * 检查当前用户是否关注了某个特定用户，并返回一个包含关注状态的 JSON 响应
     * @param userName
     * @param topicUserId
     * @return
     */
    @GetMapping("/validate/{topicUserId}")
    public ApiResult<Map<String, Object>> isFollow(@RequestHeader(value = USER_NAME) String userName,
                                                   @PathVariable("topicUserId") String topicUserId) {
        UmsUser umsUser = umsUserService.getUserByUsername(userName);
        Map<String, Object> map = new HashMap<>(16);
        map.put("hasFollow", false);
        if(!ObjectUtils.isEmpty(umsUser)) {
            BmsFollow one = bmsFollowService.getOne(new LambdaQueryWrapper<BmsFollow>()
                    .eq(BmsFollow::getFollowerId, umsUser.getId())
                    .eq(BmsFollow::getParentId, topicUserId));
            if (!ObjectUtils.isEmpty(one)) {
                map.put("hasFollow", true);
            }
        }
        return ApiResult.success(map);
    }




}

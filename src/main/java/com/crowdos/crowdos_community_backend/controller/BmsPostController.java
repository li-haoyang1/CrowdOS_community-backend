package com.crowdos.crowdos_community_backend.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.model.dto.CreateTopicDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.BmsPromotion;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.PostVO;
import com.crowdos.crowdos_community_backend.service.IBmsPostService;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.crowdos.crowdos_community_backend.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/post")
public class BmsPostController extends BaseController {
    @Resource
    private IBmsPostService iBmsPostService;

    @Resource
    private IUmsUserService iUmsUserService;

    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize){
        Page<PostVO> list = iBmsPostService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }

    /**
     * 创建帖子
     *
     * @param userName
     * @param dto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<BmsPost> create(@RequestHeader(value = USER_NAME) String userName,
                                     @RequestBody CreateTopicDTO dto){
        UmsUser user = iUmsUserService.getUserByUsername(userName);
        BmsPost topic = iBmsPostService.create(dto, user);
        return ApiResult.success(topic);
    }

    @GetMapping()
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id){
        Map<String, Object> map = iBmsPostService.viewTopic(id);
        return ApiResult.success(map);
    }

    @GetMapping("/recommend")
    public ApiResult<List<BmsPost>> getRecommend(@RequestParam("topicId") String id){
        List<BmsPost> topics = iBmsPostService.getRecommend(id);
        return ApiResult.success(topics);
    }

    @PostMapping("/update")
    //Valid注解的作用：@Valid 注解在 Java 中通常与 Spring MVC 框架结合使用，用于对方法参数或对象进行验证。
    //在 Spring MVC 的控制器方法中，@Valid 作用于带有 @RequestBody 的参数时，会对请求体反序列化得到的 Java 对象进行校验。如果校验失败，Spring 会抛出异常（如 MethodArgumentNotValidException）
    public ApiResult<BmsPost> update(@Valid @RequestBody BmsPost post,
                                     @RequestHeader(value = USER_NAME) String userName){
        UmsUser user = iUmsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getId().equals(post.getUserId()), "非本人，无修改权限");
        post.setModifyTime(new Date());
        post.setContent(EmojiParser.parseToAliases(post.getContent()));
        iBmsPostService.updateById(post);
        return ApiResult.success(post);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<Boolean> delete(@RequestHeader(value = USER_NAME) String userName,
                                     @PathVariable("id") String id){
        UmsUser user = iUmsUserService.getUserByUsername(userName);
        BmsPost post = iBmsPostService.getById(id);
        Assert.notNull(post, "帖子已删除");
        Assert.isTrue(user.getId().equals(post.getUserId()), "非发帖本人，无法删除帖子");
        iBmsPostService.removeById(id);
        return ApiResult.success(true,"删除成功");
    }

}



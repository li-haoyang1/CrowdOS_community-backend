package com.crowdos.crowdos_community_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.mapper.BmsTagMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.BmsTag;
import com.crowdos.crowdos_community_backend.service.IBmsTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tag")
public class BmsTagController extends BaseController{

    @Resource
    private IBmsTagService iBmsTagService;

    @GetMapping("/{name}")
    public ApiResult<Map<String,Object>> getTopicsByTag(@PathVariable("name") String name,
                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
        Map<String,Object> map = new HashMap<>(16);

        LambdaQueryWrapper<BmsTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BmsTag::getName, name);
        BmsTag bmsTag = iBmsTagService.getOne(wrapper);

        Assert.notNull(bmsTag,"该话题已经不存在了");

        Page<BmsPost> topics = iBmsTagService.selectTopicsById(new Page<>(page,size), bmsTag.getId());

        //其他热门标签
        Page<BmsTag> hotTags = iBmsTagService.page(new Page<>(1,10),
                new LambdaQueryWrapper<BmsTag>()
                        .notIn(BmsTag::getName,name)
                        .orderByDesc(BmsTag::getTopicCount));

        map.put("topics",topics);
        map.put("hotTags",hotTags);

        return ApiResult.success(map);

    }
}

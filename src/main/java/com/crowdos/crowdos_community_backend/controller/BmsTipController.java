package com.crowdos.crowdos_community_backend.controller;

import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.model.entity.BmsTip;
import com.crowdos.crowdos_community_backend.service.IBmsTipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tip")
public class BmsTipController extends BaseController {
    @Resource
    private IBmsTipService bmsTipService;

    @GetMapping("/today")
    public ApiResult<BmsTip> getRandomTip(){
        BmsTip tip = bmsTipService.getRandomTip();
        return ApiResult.success(tip);
    }
}
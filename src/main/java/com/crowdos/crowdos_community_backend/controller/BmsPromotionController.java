package com.crowdos.crowdos_community_backend.controller;

import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.model.entity.BmsPromotion;
import com.crowdos.crowdos_community_backend.service.IBmsPromotionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/promotion")
public class BmsPromotionController extends BaseController {
    @Resource
    private IBmsPromotionService bmsPromotionService;
    @GetMapping("/all")
    public ApiResult<List<BmsPromotion>> getAllPromotion() {
        List<BmsPromotion> promotions = bmsPromotionService.list();
        return ApiResult.success(promotions);
    }
}
package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsPromotionMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsPromotion;
import com.crowdos.crowdos_community_backend.service.IBmsPromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IBmsPromotionServiceImpl extends ServiceImpl<BmsPromotionMapper
        , BmsPromotion> implements IBmsPromotionService {
}
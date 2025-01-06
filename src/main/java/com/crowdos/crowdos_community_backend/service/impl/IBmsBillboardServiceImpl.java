package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsBillboardMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsBillboard;
import com.crowdos.crowdos_community_backend.service.IBmsBillboardService;
import org.springframework.stereotype.Service;

@Service
public class IBmsBillboardServiceImpl extends ServiceImpl<BmsBillboardMapper
        , BmsBillboard> implements IBmsBillboardService {

}
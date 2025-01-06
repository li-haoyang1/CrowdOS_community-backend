package com.crowdos.crowdos_community_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.mapper.BmsFollowMapper;
import com.crowdos.crowdos_community_backend.model.entity.BmsFollow;
import com.crowdos.crowdos_community_backend.service.IBmsFollowService;
import org.springframework.stereotype.Service;

@Service
public class IBmsFollowServiceImpl extends ServiceImpl<BmsFollowMapper, BmsFollow> implements IBmsFollowService {

}

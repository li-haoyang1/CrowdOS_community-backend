package com.crowdos.crowdos_community_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowdos.crowdos_community_backend.model.entity.BmsTip;

public interface IBmsTipService extends IService<BmsTip> {
    BmsTip getRandomTip();
}

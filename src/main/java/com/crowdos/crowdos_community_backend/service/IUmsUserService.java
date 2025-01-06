package com.crowdos.crowdos_community_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowdos.crowdos_community_backend.model.dto.LoginDTO;
import com.crowdos.crowdos_community_backend.model.dto.RegisterDTO;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.ProfileVO;

public interface IUmsUserService extends IService<UmsUser> {

    /**
     * 注册功能
     *
     * @param dto
     * @return 注册对象
     */
    UmsUser executeRegister(RegisterDTO dto);
    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return dbUser
     */
    UmsUser getUserByUsername(String username);
    /**
     * 登录功能
     *
     * @param dto
     * @return 生成的JWT的token
     */
    String executeLogin(LoginDTO dto);
    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    ProfileVO getUserProfile(String id);
}

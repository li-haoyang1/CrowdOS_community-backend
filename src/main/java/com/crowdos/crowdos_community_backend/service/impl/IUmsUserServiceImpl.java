package com.crowdos.crowdos_community_backend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowdos.crowdos_community_backend.common.exception.ApiAsserts;
import com.crowdos.crowdos_community_backend.jwt.JwtUtil;
import com.crowdos.crowdos_community_backend.mapper.BmsFollowMapper;
import com.crowdos.crowdos_community_backend.mapper.BmsTopicMapper;
import com.crowdos.crowdos_community_backend.mapper.UmsUserMapper;
import com.crowdos.crowdos_community_backend.model.dto.LoginDTO;
import com.crowdos.crowdos_community_backend.model.dto.RegisterDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsFollow;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.model.vo.ProfileVO;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import com.crowdos.crowdos_community_backend.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IUmsUserServiceImpl extends ServiceImpl<UmsUserMapper,UmsUser> implements IUmsUserService {

    @Autowired
    private BmsTopicMapper bmsTopicMapper;

    @Autowired
    private BmsFollowMapper bmsFollowMapper;

    @Override
    public UmsUser executeRegister(RegisterDTO dto) {
        //查询是否有相同用户名
        LambdaQueryWrapper<UmsUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsUser::getUsername, dto.getName()).or().eq(UmsUser::getEmail, dto.getEmail());
        UmsUser umsUser = baseMapper.selectOne(wrapper);
        if(!ObjectUtils.isEmpty(umsUser)){
            ApiAsserts.fail("账号或邮箱已存在！");
        }
        UmsUser addUser = UmsUser.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .email(dto.getEmail())
                .password(MD5Utils.getPwd(dto.getPass()))
                .createTime(new Date())
                .status(true)
                .build();
        baseMapper.insert(addUser);

        return addUser;
    }

    @Override
    public UmsUser getUserByUsername(String username){
        return baseMapper.selectOne(new LambdaQueryWrapper<UmsUser>().eq(UmsUser::getUsername, username));
    }

    @Override
    public String executeLogin(LoginDTO dto){
        String token = null;
        try {
            UmsUser user = getUserByUsername(dto.getUsername());
            String encodePwd = MD5Utils.getPwd(dto.getPassword());
            if(!encodePwd.equals(user.getPassword())){
                throw new Exception("密码错误");
            }
            token = JwtUtil.generateToken(String.valueOf(user.getUsername()));
        } catch (Exception e) {
            log.warn("用户不存在或者密码错误", dto.getUsername());
        }
        return token;
    }

    @Override
    public ProfileVO getUserProfile(String id) {
        ProfileVO profile = new ProfileVO();
        UmsUser user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, profile);

        //用户文章数
        int count = bmsTopicMapper.selectCount(new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId, id));
        profile.setTopicCount(count);

        //粉丝数
        int follower = bmsFollowMapper.selectCount(new LambdaQueryWrapper<BmsFollow>().eq(BmsFollow::getFollowerId, id));
        profile.setFollowerCount(follower);

        return profile;
    }

}

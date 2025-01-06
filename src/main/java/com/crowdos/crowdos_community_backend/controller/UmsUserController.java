package com.crowdos.crowdos_community_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowdos.crowdos_community_backend.common.api.ApiResult;
import com.crowdos.crowdos_community_backend.model.dto.LoginDTO;
import com.crowdos.crowdos_community_backend.model.dto.RegisterDTO;
import com.crowdos.crowdos_community_backend.model.entity.BmsPost;
import com.crowdos.crowdos_community_backend.model.entity.UmsUser;
import com.crowdos.crowdos_community_backend.service.IBmsPostService;
import com.crowdos.crowdos_community_backend.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.crowdos.crowdos_community_backend.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController {
    @Resource
    private IUmsUserService umsUserService;

    @Resource
    private IBmsPostService iBmsPostService;

    /**
     * 注册用户
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ApiResult<Map<String,Object>> register(@Valid @RequestBody RegisterDTO dto){
        UmsUser user = umsUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)){
            return ApiResult.failed("账号注册失败");
        }
        Map<String,Object> map = new HashMap<>(16);
        map.put("user",user);
        return ApiResult.success(map);
    }

    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String,String>> login(@Valid @RequestBody LoginDTO dto){
        String token = umsUserService.executeLogin(dto);
        if(ObjectUtils.isEmpty(token)){
            return ApiResult.failed("账号密码错误");
        }
        Map<String,String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map,"登陆成功");
    }

    /**
     * 用户校验（JWT）
     *
     * @param userName
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<UmsUser> getUser(@RequestHeader(value = USER_NAME) String userName){
        UmsUser user = umsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut(){
        return ApiResult.success("注销成功");
    }

    @RequestMapping(value = "/{username}")
    public ApiResult<Map<String,Object>> getUserByName(@PathVariable("username") String userName,
                                                       @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "size",defaultValue = "10") Integer size){
        Map<String,Object> map = new HashMap<>(16);
        UmsUser user = umsUserService.getUserByUsername(userName);
        Assert.notNull(user, "用户不存在");
        Page<BmsPost> page = iBmsPostService.page(new Page<>(pageNo,size),
                new LambdaQueryWrapper<BmsPost>().eq(BmsPost::getUserId,user.getId()));
        map.put("user",user);
        map.put("topics",page);
        return ApiResult.success(map);

    }
    @PostMapping("/update")
    public ApiResult<UmsUser> updateUser(@RequestBody UmsUser umsUser) {
        umsUserService.updateById(umsUser);
        return ApiResult.success(umsUser);
    }


}
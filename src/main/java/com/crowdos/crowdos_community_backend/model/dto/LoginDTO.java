package com.crowdos.crowdos_community_backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
public class LoginDTO {

    @NotEmpty(message = "用户名不为空")
    @Size(min = 2, max = 15, message = "登录用户名长度在2-15")
    private String username;

    @NotEmpty(message = "密码不为空")
    @Size(min = 6, max = 20,message = "密码长度在6-20")
    private String password;

    private Boolean rememberMe;
}

package com.shanyangcode.infinitechat.authenticationservice.controller;

import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarResponse;
import com.shanyangcode.infinitechat.authenticationservice.service.impl.UserServiceImpl;
import com.shanyangcode.infinitechat.authenticationservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName UserController
 * @Description 用户类路由
 * @Author XiaoLuo
 * @Date 2025/7/24 17:06
 */

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    //用户注册
    @PostMapping("/register")
    public Result<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = userService.register(registerRequest);
        return Result.OK(response);
    }

    //用户通过密码登录
    @PostMapping("/loginByPassword")
    public Result<LoginResponse> loginByPassword(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.loginByPassword(loginRequest);
        return Result.OK(response);
    }

    //用户通过验证码登录
    @PostMapping("/loginByCode")
    public Result<LoginCodeResponse> loginByCode(@RequestBody LoginCodeRequest loginCodeRequest) {
        LoginCodeResponse response = userService.loginByVerifyCode(loginCodeRequest);
        return Result.OK(response);
    }

    //更新用户头像
    @PatchMapping("/avatar")
    public Result<UpdateAvatarResponse> updateAvatar(@Valid @RequestBody UpdateAvatarRequest request,
                                                     @RequestHeader String Authorization) {
        String id = JwtUtil.parse(Authorization).getSubject();
        UpdateAvatarResponse updateAvatarResponse = userService.updateAvatar(id, request);
        return Result.OK(updateAvatarResponse);
    }


}

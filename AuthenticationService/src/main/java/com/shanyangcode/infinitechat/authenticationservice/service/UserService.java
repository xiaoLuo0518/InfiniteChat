package com.shanyangcode.infinitechat.authenticationservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarResponse;
import com.shanyangcode.infinitechat.authenticationservice.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;

/**
 * @author 雒勇涛
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2025-07-25 10:25:44
 */
public interface UserService extends IService<User> {

    default User getOnly(QueryWrapper<User> wrapper, boolean throwEx) {
        wrapper.last("limit 1");
        return this.getOne(wrapper,throwEx);
    }

    //用户注册
    RegisterResponse register(RegisterRequest registerRequest);

    //用户通过密码登录
    LoginResponse loginByPassword(LoginRequest loginRequest);

    //用户通过验证码登录
    LoginCodeResponse loginByVerifyCode(LoginCodeRequest loginCodeRequest);


    //修改(上传)头像
    UpdateAvatarResponse updateAvatar(String id,@Valid UpdateAvatarRequest request);
}

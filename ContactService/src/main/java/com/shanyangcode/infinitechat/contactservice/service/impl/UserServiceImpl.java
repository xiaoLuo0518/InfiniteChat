package com.shanyangcode.infinitechat.contactservice.service.impl;

import com.shanyangcode.infinitechat.contactservice.data.User.User;
import com.shanyangcode.infinitechat.contactservice.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 16:37
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUser() {
        return new User().setName("666");
    }
}

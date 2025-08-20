package com.shanyangcode.infinitechat.messagingservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.mapper.UserMapper;
import com.shanyangcode.infinitechat.messagingservice.model.User;
import com.shanyangcode.infinitechat.messagingservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 20:36
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
}

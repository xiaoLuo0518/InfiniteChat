package com.shanyangcode.infinitechat.messagingservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyangcode.infinitechat.messagingservice.model.User;

public interface UserService extends IService<User> {
    User getById(Long userId);
}

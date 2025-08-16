package com.shanyangcode.infinitechat.messagingservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyangcode.infinitechat.messagingservice.model.Session;

public interface SessionService extends IService<Session> {
    Session getById(Long sessionId);
}

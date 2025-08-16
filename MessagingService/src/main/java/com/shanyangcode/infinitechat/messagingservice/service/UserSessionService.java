package com.shanyangcode.infinitechat.messagingservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyangcode.infinitechat.messagingservice.model.UserSession;

import java.util.List;

public interface UserSessionService extends  IService<UserSession> {
    List<Long> getUserIdsBySessionId(Long sessionId);
}

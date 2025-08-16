package com.shanyangcode.infinitechat.messagingservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.mapper.UserSessionMapper;
import com.shanyangcode.infinitechat.messagingservice.model.UserSession;
import com.shanyangcode.infinitechat.messagingservice.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UserSessionServiceImpl
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 21:30
 */
@Service
public class UserSessionServiceImpl extends ServiceImpl<UserSessionMapper, UserSession> implements UserSessionService {

    @Autowired
    private UserSessionMapper userSessionMapper;

    @Override
    public List<Long> getUserIdsBySessionId(Long sessionId) {
        List<UserSession> userSessionList = this.lambdaQuery().eq(UserSession::getSessionId, sessionId).list();
        return userSessionList.stream()
                .map(UserSession::getUserId)
                .collect(Collectors.toList());
    }
}

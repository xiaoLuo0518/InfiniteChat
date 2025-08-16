package com.shanyangcode.infinitechat.messagingservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.mapper.SessionMapper;
import com.shanyangcode.infinitechat.messagingservice.model.Session;
import com.shanyangcode.infinitechat.messagingservice.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SessionServiceimpl
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/16 12:01
 */
@Service
public class SessionServiceimpl extends ServiceImpl<SessionMapper, Session> implements SessionService {
    @Autowired
    private SessionMapper sessionMapper;


    public Session getById(Long sessionId) {
        return sessionMapper.selectById(sessionId);
    }
}

package com.shanyangcode.infinitechat.realtimecommunicationservice.service.serviceImpl;

import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageResponse;
import com.shanyangcode.infinitechat.realtimecommunicationservice.service.NettyMessageService;
import com.shanyangcode.infinitechat.realtimecommunicationservice.service.RcvMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName RvcMsgService
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 20:04
 */
@Service
@Slf4j
public class RcvMsgServiceImpl implements RcvMsgService {
    @Autowired
    NettyServiceMessageImpl nettyServiceMessage;

    @Override
    public ReceiveMessageResponse receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
        nettyServiceMessage.sendMessageToUser(receiveMessageRequest);
        return null;
    }
}

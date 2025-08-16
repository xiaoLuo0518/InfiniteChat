package com.shanyangcode.infinitechat.realtimecommunicationservice.service;

import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageResponse;

public interface RcvMsgService {
    public ReceiveMessageResponse receiveMessage(ReceiveMessageRequest receiveMessageRequest);
}

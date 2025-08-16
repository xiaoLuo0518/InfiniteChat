package com.shanyangcode.infinitechat.realtimecommunicationservice.service;

import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageResponse;

public interface RcvMsgServer {
    ReceiveMessageResponse receiveMessage(ReceiveMessageRequest request);
}
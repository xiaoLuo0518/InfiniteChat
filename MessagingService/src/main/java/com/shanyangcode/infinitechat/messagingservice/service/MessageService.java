package com.shanyangcode.infinitechat.messagingservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgRequest;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgResponse;
import com.shanyangcode.infinitechat.messagingservice.model.Message;

public interface MessageService extends IService<Message> {
    SendMsgResponse sendMessage(SendMsgRequest sendMsgRequest);
}

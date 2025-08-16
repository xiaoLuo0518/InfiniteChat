package com.shanyangcode.infinitechat.messagingservice.controller;

import com.shanyangcode.infinitechat.messagingservice.common.Result;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgRequest;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgResponse;
import com.shanyangcode.infinitechat.messagingservice.feign.ContactServiceFeign;
import com.shanyangcode.infinitechat.messagingservice.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ContactController
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 11:45
 */
@RestController
@RequestMapping("api/v1/")
public class ContactController {
    @Autowired
    private ContactServiceFeign contactServiceFeign;

    @Autowired
    private MessageServiceImpl messageService;

    @GetMapping("/feign")
    public Result<?> getUser() {
        Result<?> user = contactServiceFeign.getUser();
        return Result.OK(user);
    }

    @PostMapping("chat/session")
    public Result<SendMsgResponse> sendMessage(SendMsgRequest sendMsgRequest) {
        SendMsgResponse response = messageService.sendMessage(sendMsgRequest);
        return Result.OK(response);
    }

}

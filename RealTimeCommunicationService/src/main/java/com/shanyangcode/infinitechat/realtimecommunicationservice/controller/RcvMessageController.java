package com.shanyangcode.infinitechat.realtimecommunicationservice.controller;

import com.shanyangcode.infinitechat.realtimecommunicationservice.common.Result;
import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageRequest;
import com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage.ReceiveMessageResponse;
import com.shanyangcode.infinitechat.realtimecommunicationservice.service.RcvMsgService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RcvMessageController
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 17:03
 */
@RestController
@RequestMapping("api/v1/message")
@RequiredArgsConstructor
public class RcvMessageController {
    @Autowired
    private final RcvMsgService rcvMsgService;



    @PostMapping("/user")
    public Result<ReceiveMessageResponse> receiveMessage( @RequestBody ReceiveMessageRequest request) {
        ReceiveMessageResponse response = rcvMsgService.receiveMessage(request);
        return Result.OK(response);
    }


}

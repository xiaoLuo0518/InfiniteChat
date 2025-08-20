package com.shanyangcode.infinitechat.messagingservice.controller;

import com.shanyangcode.infinitechat.messagingservice.common.Result;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketRequest;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketResponse;
import com.shanyangcode.infinitechat.messagingservice.service.impl.RedPacketServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.util.annotation.PreventDuplicateSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RedPacketController
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 18:25
 */

@RestController
@RequestMapping("api/v1/chat/redPacket")
public class RedPacketController {

    private final RedPacketServiceImpl redPacketService;

    @Autowired
    public RedPacketController(RedPacketServiceImpl redPacketService) {
        this.redPacketService = redPacketService;
    }

    @PostMapping("/send")
    @PreventDuplicateSubmit //防止重复提交
    public Result<SendRedPacketResponse> sendRedPacket(@RequestBody SendRedPacketRequest sendRedPacketRequest) {
        SendRedPacketResponse redPacketResponse = redPacketService.sendRedPacket(sendRedPacketRequest);
        return Result.OK(redPacketResponse);
    }
}

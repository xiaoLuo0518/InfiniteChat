package com.shanyangcode.infinitechat.messagingservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketRequest;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketResponse;
import com.shanyangcode.infinitechat.messagingservice.model.RedPacket;

public interface RedPacketService extends IService<RedPacket> {
    SendRedPacketResponse sendRedPacket(SendRedPacketRequest sendRedPacketRequest);

    void handlerExpiredRedPacked(Long redPackedId);
}

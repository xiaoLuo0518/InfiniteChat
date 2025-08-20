package com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName RedPacketMessageBody
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/20 14:00
 */
@Data
@Accessors(chain = true)
public class RedPacketMessageBody {

    private String content;

    private String redPacketWrapperText;
}

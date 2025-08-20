package com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName ResponseMsgVo
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 18:29
 */
@Data
@Accessors(chain = true)
public class SendRedPacketResponse {
    private String sessionId;

    private Integer sessionType;

    private Integer type;

    private Long messageId;

    private Object body;

    private String createdAt;
}
package com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @ClassName SendRedPacketRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 18:28
 */
@Data
@Accessors(chain = true)
public class SendRedPacketRequest {
    private Long sessionId;

    private Long sendUserId;

    private Long receiveUserId;

    private Integer type;

    private Integer sessionType;

    private Body body;

    @Data
    @Accessors(chain = true)
    public static class Body {

        private Integer redPacketType;

        private BigDecimal totalAmount;

        private Integer totalCount;

        private String redPacketWrapperText;
    }
}
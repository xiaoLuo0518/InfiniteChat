package com.shanyangcode.infinitechat.messagingservice.data.sendMsg;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SendMsgResponse
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 17:42
 */
@Data
@Accessors(chain = true)
public class SendMsgResponse {
    private String sessionId;

    private Integer sessionType;

    private Integer type;

    private Long messageId;

    private Object body;

    private String createdAt;
}

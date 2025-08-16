package com.shanyangcode.infinitechat.messagingservice.data.sendMsg;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SendMsgRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 17:36
 */
@Data
@Accessors(chain = true)
public class SendMsgRequest {
    private Long sessionId;
    private Long sendUserId;
    private Integer sessionType;
    private Integer type;
    private Long receiverUserId;
    private Object data;
}

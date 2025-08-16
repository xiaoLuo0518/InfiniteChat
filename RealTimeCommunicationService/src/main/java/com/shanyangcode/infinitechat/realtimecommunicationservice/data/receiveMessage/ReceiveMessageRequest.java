package com.shanyangcode.infinitechat.realtimecommunicationservice.data.receiveMessage;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName ReceiveMessageRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 17:09
 */
@Data
@Accessors(chain = true)
public class ReceiveMessageRequest {
    private List<Long> receiveUserIds;

    private String sendUserId;

    private String sessionId;

    private String avatar;

    private String userName;

    private Integer type;

    private String messageId;

    private Integer sessionType;

    private String sessionName;

    private String sessionAvatar;

    private String createdAt;

    private Object body;
}

package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName Message
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 20:34
 */
@Data
@Accessors(chain = true)
public class Message {
    protected List<Long> receiveUserIds;

    protected String sendUserId;

    protected String sessionId;

    protected String avatar;

    protected String userName;

    protected Integer type;

    protected String messageId;

    protected Integer sessionType;

    protected String sessionName;

    protected String sessionAvatar;

    protected String createdAt;

    protected Object body;
}
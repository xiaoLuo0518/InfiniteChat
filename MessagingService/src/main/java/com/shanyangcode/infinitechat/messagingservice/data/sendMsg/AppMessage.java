package com.shanyangcode.infinitechat.messagingservice.data.sendMsg;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName AppMessage
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/16 11:40
 */
@Data
@Accessors(chain = true)
public class AppMessage {

    protected Long sessionId;

    protected List<Long> receiveUserIds;

    protected Long sendUserId;

    protected String userName;

    protected String avatar;

    protected Integer type;

    protected Long messageId;

    protected Integer sessionType;

    protected String sessionName;

    protected String sessionAvatar;

    private String createdAt;

    protected Object body;
}

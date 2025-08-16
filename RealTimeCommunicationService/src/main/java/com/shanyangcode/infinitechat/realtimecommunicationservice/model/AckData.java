package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName AckData
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 11:18
 */
@Data
@Accessors(chain = true)
public class AckData {
    private Long sessionId;
    private Long receiverUserUuid;
    private String msgUuid;
}

package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName TextMessageBody
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 20:35
 */
@Data
@Accessors(chain = true)
public class TextMessageBody {
    private String content;
    private String replyId;
}

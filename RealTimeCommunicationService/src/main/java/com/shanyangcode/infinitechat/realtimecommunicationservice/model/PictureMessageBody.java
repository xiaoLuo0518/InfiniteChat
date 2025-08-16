package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName PictureMessageBody
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/31 09:35
 */
@Data
@Accessors(chain = true)
public class PictureMessageBody {

    private Integer size;

    private String url;

    private String replyId;
}

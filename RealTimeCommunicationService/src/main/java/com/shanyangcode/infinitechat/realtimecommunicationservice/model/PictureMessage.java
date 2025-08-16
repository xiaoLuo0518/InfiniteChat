package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName PictureMessage
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/31 09:34
 */
@Data
@Accessors(chain = true)
public class PictureMessage extends Message {

    private PictureMessageBody body;

    @Override
    public String toString() {
        return super.toString();
    }
}


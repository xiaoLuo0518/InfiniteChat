package com.shanyangcode.infinitechat.realtimecommunicationservice.constants;

import lombok.Getter;

/**
 * @ClassName MessageRecTypeEnum
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 20:22
 */
@Getter
public enum MessageRecTypeEnum {
    TEXT_MESSAGE(1),
    PICTURE_MESSAGE(2),
    FILE_MESSAGE(3),
    VIDEO_MESSAGE(4),
    RED_PACKET_MESSAGE(5),
    EMOTICON_MESSAGE(6);


    private final Integer code;

    MessageRecTypeEnum(Integer code) {
        this.code = code;
    }

    public static MessageRecTypeEnum fromCode(int code) {
        for (MessageRecTypeEnum type : MessageRecTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

}

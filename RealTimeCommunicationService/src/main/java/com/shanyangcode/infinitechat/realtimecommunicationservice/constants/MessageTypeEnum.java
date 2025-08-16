package com.shanyangcode.infinitechat.realtimecommunicationservice.constants;


import lombok.Data;
import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    ACK(1), LOG_OUT(2), HEART_BEAT(5),
    ILLEGAL(99);


    private final Integer code;

    MessageTypeEnum(Integer code) {
        this.code = code;
    }

    public static MessageTypeEnum of(Integer type) {
        switch (type) {
            case 1:
                return ACK;
            case 2:
                return LOG_OUT;
            case 5:
                return HEART_BEAT;
            default:
                return ILLEGAL;
        }
    }


}

package com.shanyangcode.infinitechat.messagingservice.constants;

import lombok.Getter;

@Getter
public enum SessionType {
    SINGLE(1),
    GROUP(2);
    private final Integer value;

    SessionType(Integer value) {
        this.value = value;
    }

}

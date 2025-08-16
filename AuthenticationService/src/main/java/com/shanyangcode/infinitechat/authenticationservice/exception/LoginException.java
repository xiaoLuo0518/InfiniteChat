package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {
    private int code;

    public LoginException(String message, int code) {
        super(message);
        this.code = code;
    }

    public LoginException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public LoginException(String message) {
        super(message);
    }
}

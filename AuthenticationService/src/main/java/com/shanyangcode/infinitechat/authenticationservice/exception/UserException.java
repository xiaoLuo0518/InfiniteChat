package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import lombok.Getter;


@Getter
public class UserException extends RuntimeException {

    private final int code;

    public UserException(String message) {
        super(message);
        this.code = ErrorEnum.SYSTEM_ERROR.getCode();
    }

    public UserException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

    public UserException(String message,int code) {
        super(message);
        this.code = code;
    }


    public UserException(ErrorEnum errorEnum, String message) {
        super(message);
        this.code = errorEnum.getCode();
    }

}

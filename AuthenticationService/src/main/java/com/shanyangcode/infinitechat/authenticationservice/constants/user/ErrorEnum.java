package com.shanyangcode.infinitechat.authenticationservice.constants.user;


import lombok.Getter;

@Getter
public enum ErrorEnum {

    //400 01 注册异常   *****
    //500 01 数据库异常
    //
    REGISTER_ERROR(40001,"用户已存在"),
    Code_ERROR(40002,"验证码错误"),
    LOGIN_USER_NOT_EXIST(40003, "用户不存在"),
    LOGIN_PASSWORD_ERROR(40004, "密码错误"),
    UPDATE_AVATAR_ERROR(40005,"更新头像错误"),
    SUCCESS(200,"ok"),
    SYSTEM_ERROR(50000,"系统内部异常");


    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}

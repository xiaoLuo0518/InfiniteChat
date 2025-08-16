package com.shanyangcode.infinitechat.authenticationservice.constants.verifyCode;

import lombok.Data;
import lombok.Getter;


@Getter
public enum VerifyCodeKeyStrategy {
    REGISTER("register:code:"),LOGIN("login:code:"),RESET_PASSWORD("resetPassword:code:");

    private final String verifyCode;

    VerifyCodeKeyStrategy(String verifyCode){
        this.verifyCode = verifyCode;
    }

}

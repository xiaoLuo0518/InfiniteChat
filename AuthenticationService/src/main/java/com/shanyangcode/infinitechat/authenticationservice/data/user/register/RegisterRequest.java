package com.shanyangcode.infinitechat.authenticationservice.data.user.register;

import lombok.Data;

/**
 * @ClassName register
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/25 11:23
 */

@Data
public class RegisterRequest {
    private String phone;
    private String password;
    private String code;
}

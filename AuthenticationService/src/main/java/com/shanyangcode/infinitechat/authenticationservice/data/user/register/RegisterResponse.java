package com.shanyangcode.infinitechat.authenticationservice.data.user.register;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName RegisterResponse
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/25 11:23
 */
@Data
@Accessors(chain = true)
public class RegisterResponse {
    private String phone;
}

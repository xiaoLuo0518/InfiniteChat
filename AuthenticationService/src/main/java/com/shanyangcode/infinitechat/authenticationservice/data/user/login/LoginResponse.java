package com.shanyangcode.infinitechat.authenticationservice.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName LoginRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 20:17
 */
@Data
@Accessors(chain = true)
public class LoginResponse {
    private Long userId;
    private String userName;
    private String avatar;
    private String signature;
    private Integer gender;
    private Integer status;
    private String token;
}

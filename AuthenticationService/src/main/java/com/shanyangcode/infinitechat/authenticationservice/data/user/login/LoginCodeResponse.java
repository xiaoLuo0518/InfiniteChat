package com.shanyangcode.infinitechat.authenticationservice.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName LoginCodeResponse
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/27 09:41
 */
@Data
@Accessors(chain = true)
public class LoginCodeResponse {
    private Long userId;
    private String userName;
    private String avatar;
    private String signature;
    private Integer gender;
    private Integer status;
    private String token;
}

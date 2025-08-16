package com.shanyangcode.infinitechat.authenticationservice.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @ClassName LoginRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 20:17
 */
@Data
@Accessors(chain = true)
public class LoginRequest {
    @NotEmpty(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号应为11位")
    private String phone;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6 ,max = 16, message = "密码应为6-16位字母数字组合")
    private String password;
}

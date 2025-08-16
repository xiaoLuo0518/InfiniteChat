package com.shanyangcode.infinitechat.authenticationservice.data.user.login;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @ClassName LoginCodeRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/27 09:29
 */
@Data
@Accessors(chain = true)
public class LoginCodeRequest {
    @NotEmpty(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号应为11位")
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码应为6位数字")
    private String code;
}

package com.shanyangcode.infinitechat.authenticationservice.data.common.sms;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SmsResponse
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 10:59
 */

@Data
@Accessors(chain = true)
public class SMSResponse {
    private String phone;
    private String code;

}

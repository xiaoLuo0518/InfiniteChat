package com.shanyangcode.infinitechat.authenticationservice.data.common.sms;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SmsRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 10:59
 */



@Data
@Accessors(chain = true)
public class SMSRequest {
    private String type;
    private String phone;
}

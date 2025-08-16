package com.shanyangcode.infinitechat.realtimecommunicationservice.constants.config;

/**
 * @ClassName ConfigEnum
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 21:19
 */
import lombok.Getter;

@Getter
public enum ConfigEnum {
    SMS_ACCESS_KEY_ID("smsAccessKeyId", "LTAI5t9w4h8x2HkQuBnrxWV9"),
    SMS_ACCESS_KEY_SECRET("smsAccessKeySecret","NXKO23aTAdWhJMKosJPo0HTJXkR5yn"),
    SMS_SIG_NAME("smsSigName","无夕教育科技"),
    SMS_TEMPLATE_CODE("smsTemplateCode","SMS_471490089"),
    TOKEN_SECRET_KEY("tokenSecretKey","goat");


    private final String value;
    private final String text;

    ConfigEnum(String text, String value){
        this.text = text;
        this.value = value;
    }
}
package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName HeartBeat
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 11:21
 */
@Data
@Accessors(chain = true)
public class HeartBeat {
    private Integer type;
    private String message;
    private Integer code;
}

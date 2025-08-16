package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName LogOutData
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/30 11:21
 */
@Data
@Accessors(chain = true)
public class LogOutData {
    private Integer userUuid;
}

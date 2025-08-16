package com.shanyangcode.infinitechat.realtimecommunicationservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName MessageDTO
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/29 17:56
 */
@Data
@Accessors(chain = true)
@JsonPropertyOrder({"type","data"})
public class MessageDTO {
    //类型
    private Integer type;
    //
    private Object data;
}

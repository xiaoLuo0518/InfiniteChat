package com.shanyangcode.infinitechat.messagingservice.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * @ClassName Result
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/14 11:55
 */
@Data
@Accessors(chain = true)
public class Result<T> {
    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> OK(T user) {
        return new Result<T>()
                .setCode(HttpStatus.OK.value())
                .setData(user)
                .setMsg("success");
    }
}

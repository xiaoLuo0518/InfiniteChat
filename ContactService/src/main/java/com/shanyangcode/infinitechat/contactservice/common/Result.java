package com.shanyangcode.infinitechat.contactservice.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * @ClassName result
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 17:14
 */
@Data
@Accessors(chain = true)
public class Result<T> {
    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> OK(T data){
        Result<T> r = new Result<>();

        return r.setCode(HttpStatus.OK.value()).setData(data);
    }

    public static <T> Result<T> ValidError(String msg){
        Result<T> r = new Result<T>();

        return r.setCode(HttpStatus.BAD_REQUEST.value()).setMsg(msg);
    }

    public static <T> Result<T> DatabaseError(String msg){
        Result<T> r = new Result<>();

        return r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg);
    }

    public static <T> Result<T> ServerError(String msg){
        Result<T> r = new Result<>();

        return r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg);
    }

    public static <T> Result<T> UserError(int code, String msg){
        Result<T> r = new Result<>();

        return r.setCode(code).setMsg(msg);
    }
}

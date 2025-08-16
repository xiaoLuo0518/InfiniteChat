package com.shanyangcode.infinitechat.authenticationservice.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * @ClassName Result
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/25 11:18
 */

@Data
@Accessors(chain = true)
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
    }


    //封装成功的result
    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result<>();
        return r.setCode(HttpStatus.OK.value()).setData(data);
    }


    //封装数据库操作失败的result
    public static <T> Result<T> dataBaseError(String message) {
        Result<T> r = new Result<>();
        return r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(message);
    }


    //封装数据库操作失败的result
    public static <T> Result<T> serverError(String message) {
        Result<T> r = new Result<>();
        return r.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(message);
    }


    //对用户操作失败的result
    public static <T> Result<T> userError(String message, int code) {
        Result<T> r = new Result<>();
        return r.setCode(code).setMessage(message);
    }

    //验证码失败的result
    public static <T> Result<T> codeError(String message, int code) {
        Result<T> r = new Result<>();
        return r.setCode(code).setMessage(message);
    }




}

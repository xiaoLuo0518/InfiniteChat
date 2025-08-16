package com.shanyangcode.infinitechat.authenticationservice.exception;

import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @ClassName GlobalExceptionHandler
 * @Description 统一的异常处理类
 * @Author XiaoLuo
 * @Date 2025/7/25 19:28
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    public Result<?> handler(Throwable error) {
        log.error("未知错误", error);
        return Result.serverError(error.getMessage());
    }


    @ExceptionHandler(value = UserException.class)
    public Result<?> handlerUserException(UserException error) {
        log.error("用户错误信息：{}", error.getMessage());
        return Result.userError(error.getMessage(), error.getCode());
    }

    @ExceptionHandler(value = CodeException.class)
    public Result<?> handlerCodeException(CodeException error) {
        log.error(error.getMessage());
        return Result.codeError(error.getMessage(), error.getCode());
    }


    @ExceptionHandler(value = DataBaseException.class)
    public Result<?> handlerCodeException(DataBaseException error) {
        log.error(error.getMessage());
        return Result.dataBaseError(error.getMessage());
    }

    @ExceptionHandler(value = LoginException.class)
    public Result<?> handlerLoginException(LoginException error) {
        log.error(error.getMessage());
        return new Result<>(error.getCode(),error.getMessage(),null);
    }


}

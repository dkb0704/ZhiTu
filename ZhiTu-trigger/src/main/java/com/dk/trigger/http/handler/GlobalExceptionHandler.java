package com.dk.trigger.http.handler;

import com.dk.api.response.Response;
import com.dk.types.enums.ResponseCode;
import com.dk.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：拦截 AppException 和未知异常，返回统一 Response。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：如登录失败、参数校验不通过等。
     */
    @ExceptionHandler(AppException.class)
    public Response<?> handleAppException(AppException e) {
        log.warn("业务异常 code={}, info={}", e.getCode(), e.getInfo());
        return Response.builder()
                .code(e.getCode())
                .info(e.getInfo())
                .build();
    }

    /**
     * 未知异常兜底。
     */
    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Response.builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
    }
}

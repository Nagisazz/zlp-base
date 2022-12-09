package com.nagisazz.base.config.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nagisazz.base.pojo.OperationResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class GlobalExceptionHandler {

    /**
     * 基础异常处理
     *
     * @param ex 异常
     * @return 操作失败
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    protected OperationResult handleBaseException(Exception ex) {
        CustomException exception = (CustomException) ex;
        log.error(exception.getMessage(), exception);
        return OperationResult.buildFailureResult(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public OperationResult nullPointerExceptionHandler(final NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return OperationResult.buildFailureResult(500, ex.getMessage());
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public OperationResult illegalArgumentExceptionHandler(final IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return OperationResult.buildFailureResult(ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public OperationResult exceptionHandler(final Exception ex) {
        log.error(ex.getMessage(), ex);
        return OperationResult.buildFailureResult(500, ex.getMessage());
    }

}

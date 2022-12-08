package com.nagisazz.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 返回通用封装类
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperationResult {

    /**
     * 响应状态
     */
    private int status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应体
     */
    private Object data;

    public static OperationResult buildSuccessResult(String message, Object data) {
        return new OperationResult(200, message, data);
    }

    public static OperationResult buildSuccessResult(String message) {
        return new OperationResult(200, message, null);
    }

    public static OperationResult buildFailureResult(String message) {
        return new OperationResult(400, message, null);
    }

    public static OperationResult buildFailureResult(Integer code, String message) {
        return new OperationResult(code, message, null);
    }

    public static OperationResult buildInfoResult(String message, Object data) {
        return new OperationResult(201, message, data);
    }

    public static OperationResult buildInfoResult(String message) {
        return new OperationResult(201, message, null);
    }

}

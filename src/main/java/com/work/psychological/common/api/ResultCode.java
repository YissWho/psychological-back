package com.work.psychological.common.api;

import lombok.Getter;

@Getter
/* 
 * 结果码枚举
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(400, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "禁止访问");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 
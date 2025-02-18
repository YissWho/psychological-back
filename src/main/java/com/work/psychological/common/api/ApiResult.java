package com.work.psychological.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/* 
 * 通用API结果类
 */
public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> ApiResult<T> failed(ResultCode resultCode) {
        return new ApiResult<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> ApiResult<T> unauthorized(String message) {
        return new ApiResult<>(ResultCode.UNAUTHORIZED.getCode(), message, null);
    }

    public static <T> ApiResult<T> forbidden(String message) {
        return new ApiResult<>(ResultCode.FORBIDDEN.getCode(), message, null);
    }
} 
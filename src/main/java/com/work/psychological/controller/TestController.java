package com.work.psychological.controller;

import com.work.psychological.common.api.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResult<String> test() {
        return ApiResult.success("hello");
    }
} 
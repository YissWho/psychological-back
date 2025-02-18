package com.work.psychological.controller;

import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.dto.UserLoginDTO;
import com.work.psychological.model.dto.UserRegisterDTO;
import com.work.psychological.model.entity.User;
import com.work.psychological.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    /* 
     * 用户控制器
     */

    private final UserService userService;

    @PostMapping("/register")
    public ApiResult<User> register(@RequestBody @Valid UserRegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        user.setPassword(null); // 不返回密码
        return ApiResult.success(user);
    }

    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody @Valid UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        return ApiResult.success(token);
    }

    @GetMapping("/current")
    public ApiResult<User> getCurrentUser() {
        return ApiResult.success(userService.getCurrentUser());
    }
}
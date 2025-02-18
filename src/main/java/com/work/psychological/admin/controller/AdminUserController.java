package com.work.psychological.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.entity.User;
import com.work.psychological.admin.dto.UserCreateDTO;
import com.work.psychological.admin.dto.UserUpdateDTO;
import com.work.psychological.admin.dto.UserQueryDTO;
import com.work.psychological.admin.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // 只允许管理员访问
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 分页查询用户列表
     * 支持按用户名、邮箱、角色筛选
     */
    @GetMapping
    public ApiResult<IPage<User>> listAllUsers(@Valid UserQueryDTO queryDTO) {
        return ApiResult.success(adminUserService.listAllUsers(queryDTO));
    }

    @GetMapping("/{id}")
    public ApiResult<User> getUser(@PathVariable Integer id) {
        return ApiResult.success(adminUserService.getUserById(id));
    }

    @PostMapping
    public ApiResult<User> createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        return ApiResult.success(adminUserService.createUser(createDTO));
    }

    @PutMapping
    public ApiResult<User> updateUser(@RequestBody @Valid UserUpdateDTO updateDTO) {
        return ApiResult.success(adminUserService.updateUser(updateDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteUser(@PathVariable Integer id) {
        adminUserService.deleteUser(id);
        return ApiResult.success(null);
    }
} 
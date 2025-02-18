package com.work.psychological.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.admin.dto.UserCreateDTO;
import com.work.psychological.admin.dto.UserUpdateDTO;
import com.work.psychological.admin.dto.UserQueryDTO;
import com.work.psychological.model.entity.User;
import java.util.List;

public interface AdminUserService {
    
    /**
     * 获取用户列表（分页）
     * 支持按用户名、邮箱、角色筛选
     */
    IPage<User> listAllUsers(UserQueryDTO queryDTO);
    
    /**
     * 获取用户详情
     */
    User getUserById(Integer id);
    
    /**
     * 创建新用户
     */
    User createUser(UserCreateDTO createDTO);
    
    /**
     * 更新用户信息
     */
    User updateUser(UserUpdateDTO updateDTO);
    
    /**
     * 删除用户
     */
    void deleteUser(Integer id);
} 
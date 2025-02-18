package com.work.psychological.service;

import com.work.psychological.model.dto.UserLoginDTO;
import com.work.psychological.model.dto.UserRegisterDTO;
import com.work.psychological.model.entity.User;

public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户信息
     */
    User register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return JWT token
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    User getCurrentUser();
}
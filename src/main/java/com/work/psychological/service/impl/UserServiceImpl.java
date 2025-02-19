package com.work.psychological.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.common.security.JwtTokenProvider;
import com.work.psychological.mapper.UserMapper;
import com.work.psychological.model.dto.UserLoginDTO;
import com.work.psychological.model.dto.UserRegisterDTO;
import com.work.psychological.model.entity.User;
import com.work.psychological.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
/* 
 * 用户服务实现类
 */
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /* 
     * 密码编码器
     */
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public User register(UserRegisterDTO registerDTO) {
        /* 检查用户名是否重复 */
        if (this.findByUsername(registerDTO.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        /* 检查邮箱是否重复 */
        if (this.findByEmail(registerDTO.getEmail()) != null) {
            throw new BusinessException("邮箱已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setRole("USER");
        user.setAvatar("/api/uploads/avatars/default.png");  // 使用带/api前缀的完整路径

        // 保存用户
        this.save(user);
        return user;
    }

    @Override
    public String login(UserLoginDTO loginDTO) {
        log.info("用户尝试登录: {}", loginDTO.getUsername());
        
        User user = this.findByUsername(loginDTO.getUsername());
        if (user == null) {
            log.warn("登录失败：用户名不存在 - {}", loginDTO.getUsername());
            throw new BusinessException("用户名不存在");
        }

        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
        if (!matches) {
            log.warn("登录失败：密码错误 - {}", loginDTO.getUsername());
            throw new BusinessException("密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        log.info("用户登录成功: {}", loginDTO.getUsername());
        
        // 检查用户是否为管理员，如果是则返回带role的JSON
        if ("ADMIN".equals(user.getRole())) {
            return String.format("{\"token\":\"%s\",\"role\":\"ADMIN\"}", token);
        }
        return String.format("{\"token\":\"%s\"}", token);
    }

    @Override
    public User findByUsername(String username) {
        /* 
         * 根据用户名查找用户
         */
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                   .eq(User::getIsDeleted, 0);  // 只查询未删除的用户
        return this.getOne(queryWrapper);
    }

    @Override
    public User findByEmail(String email) {
        /* 
         * 根据邮箱查找用户
         */
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                   .eq(User::getIsDeleted, 0);  // 只查询未删除的用户
        return this.getOne(queryWrapper);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null); // 出于安全考虑，不返回密码
        return user;
    }
}
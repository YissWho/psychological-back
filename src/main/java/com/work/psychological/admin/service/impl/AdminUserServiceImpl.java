package com.work.psychological.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.psychological.admin.dto.UserCreateDTO;
import com.work.psychological.admin.dto.UserUpdateDTO;
import com.work.psychological.admin.service.AdminUserService;
import com.work.psychological.common.dto.PageRequestDTO;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.UserMapper;
import com.work.psychological.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.work.psychological.admin.dto.UserQueryDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public IPage<User> listAllUsers(UserQueryDTO queryDTO) {
        // 创建分页对象
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 创建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        // 用户名模糊查询
        if (StringUtils.hasText(queryDTO.getUsername())) {
            queryWrapper.like(User::getUsername, queryDTO.getUsername());
        }
        
        // 邮箱模糊查询
        if (StringUtils.hasText(queryDTO.getEmail())) {
            queryWrapper.like(User::getEmail, queryDTO.getEmail());
        }
        
        // 角色精确匹配
        if (StringUtils.hasText(queryDTO.getRole())) {
            queryWrapper.eq(User::getRole, queryDTO.getRole());
        }
        
        // 只查询未删除的用户
        queryWrapper.eq(User::getIsDeleted, 0);
        
        // 按创建时间升序排序
        queryWrapper.orderByAsc(User::getCreatedAt);

        // 执行分页查询
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
        
        // 清除密码信息
        userPage.getRecords().forEach(user -> user.setPassword(null));
        
        return userPage;
    }

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null); // 出于安全考虑，不返回密码
        return user;
    }

    @Override
    @Transactional
    public User createUser(UserCreateDTO createDTO) {
        // 检查用户名是否已存在
        if (checkUsernameExists(createDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (checkEmailExists(createDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(createDTO, user);

        // 设置密码：如果没有提供则使用默认密码
        String password = StringUtils.hasText(createDTO.getPassword())
                ? createDTO.getPassword()
                : "123456";
        user.setPassword(passwordEncoder.encode(password));

        // 设置默认头像（如果没有提供）
        if (!StringUtils.hasText(user.getAvatar())) {
            user.setAvatar("/uploads/avatars/default.png");  // 使用带/api前缀的完整路径
        }

        userMapper.insert(user);

        log.info("管理员创建了新用户: {}, 使用{}密码",
                user.getUsername(),
                StringUtils.hasText(createDTO.getPassword()) ? "自定义" : "默认");

        user.setPassword(null); // 不返回密码
        return user;
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDTO updateDTO) {
        // 检查用户是否存在
        User existingUser = userMapper.selectById(updateDTO.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 如果修改了用户名，检查新用户名是否已存在
        if (StringUtils.hasText(updateDTO.getUsername()) 
            && !updateDTO.getUsername().equals(existingUser.getUsername())
            && checkUsernameExists(updateDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 如果修改了邮箱，检查新邮箱是否已存在
        if (StringUtils.hasText(updateDTO.getEmail()) 
            && !updateDTO.getEmail().equals(existingUser.getEmail())
            && checkEmailExists(updateDTO.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(updateDTO, user);
        
        // 如果提供了新密码，则加密新密码
        if (StringUtils.hasText(updateDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        userMapper.updateById(user);
        
        log.info("管理员更新了用户信息: {}", user.getUsername());
        
        // 重新查询并返回最新数据
        User updatedUser = userMapper.selectById(updateDTO.getId());
        updatedUser.setPassword(null); // 不返回密码
        return updatedUser;
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否为管理员
        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException("不能删除管理员账号");
        }
        
        userMapper.deleteById(id);
        log.info("管理员删除了用户: {}", user.getUsername());
    }

    private boolean checkUsernameExists(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    private boolean checkEmailExists(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return userMapper.selectCount(queryWrapper) > 0;
    }
} 
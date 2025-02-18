package com.work.psychological.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {
    
    @NotNull(message = "用户ID不能为空")
    private Integer id;
    
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    private String username;
    
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;  // 可选，如果不修改密码则不传
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String role;
    
    private String avatar;
} 
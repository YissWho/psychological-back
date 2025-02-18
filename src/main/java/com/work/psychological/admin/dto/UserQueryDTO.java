package com.work.psychological.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.work.psychological.common.dto.PageRequestDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageRequestDTO {
    
    /**
     * 用户名(可选，支持模糊查询)
     */
    private String username;
    
    /**
     * 邮箱(可选，支持模糊查询)
     */
    private String email;
    
    /**
     * 角色(可选，精确匹配)
     * 可选值：USER, ADMIN
     */
    private String role;
} 
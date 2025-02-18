package com.work.psychological.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    /* 
     * 用户实体类
     */

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String avatar;
    
    private String role;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableLogic
    private Integer isDeleted;
} 
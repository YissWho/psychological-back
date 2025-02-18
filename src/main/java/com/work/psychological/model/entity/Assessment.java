package com.work.psychological.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("assessments")
public class Assessment {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String title;
    
    private String description;
    
    private String questionnaire;  // JSON格式的问卷内容
    
    private String version;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    private Integer isActive;
} 
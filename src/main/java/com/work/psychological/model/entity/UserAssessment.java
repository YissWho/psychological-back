package com.work.psychological.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_assessments")
public class UserAssessment {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private Integer userId;
    
    private Integer assessmentId;
    
    private String answers;  // JSON格式的答案
    
    private Integer totalScore;
    
    private String resultCategory;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)  // 表示该字段不在数据库表中
    private String assessmentTitle;  // 问卷标题
} 
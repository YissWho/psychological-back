package com.work.psychological.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("psychological_records")
public class PsychologicalRecord {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private Integer userId;
    
    private String name;
    
    private String gender;
    
    private Integer age;
    
    private String occupation;
    
    private String stressLevel;
    
    private String sleepQuality;
    
    private String emotionalState;
    
    private String notes;
    
    private LocalDateTime lastAssessmentDate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer isDeleted;
} 
package com.work.psychological.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessmentCreateDTO {
    
    @NotBlank(message = "问卷标题不能为空")
    private String title;
    
    @NotBlank(message = "问卷描述不能为空")
    private String description;
    
    @NotNull(message = "问卷内容不能为空")
    private Object questionnaire;  // 使用Object类型接收JSON对象
    
    @NotBlank(message = "版本号不能为空")
    private String version;
} 
package com.work.psychological.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class PsychologicalRecordDTO {
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    
    @NotBlank(message = "性别不能为空")
    private String gender;
    
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;
    
    private String occupation;
    
    @NotBlank(message = "压力水平不能为空")
    private String stressLevel;
    
    @NotBlank(message = "睡眠质量不能为空")
    private String sleepQuality;
    
    @NotBlank(message = "情绪状态不能为空")
    private String emotionalState;
    
    private String notes;
} 
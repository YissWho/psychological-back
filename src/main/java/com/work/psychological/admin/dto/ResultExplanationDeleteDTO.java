package com.work.psychological.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultExplanationDeleteDTO {
    
    @NotNull(message = "问卷ID不能为空")
    private Integer assessmentId;
    
    @NotBlank(message = "结果类别不能为空")
    private String category;
} 
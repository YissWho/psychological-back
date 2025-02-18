package com.work.psychological.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultExplanationCreateDTO {
    
    @NotNull(message = "问卷ID不能为空")
    private Integer assessmentId;
    
    @NotNull(message = "最小分数不能为空")
    private Integer minScore;
    
    @NotNull(message = "最大分数不能为空")
    private Integer maxScore;
    
    @NotBlank(message = "结果类别不能为空")
    private String category;
    
    @NotBlank(message = "结果描述不能为空")
    private String description;
    
    @NotBlank(message = "建议不能为空")
    private String recommendation;
} 
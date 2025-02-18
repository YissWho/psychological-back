package com.work.psychological.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AssessmentResultVO {
    
    private Integer id;
    
    private Integer assessmentId;
    
    private String assessmentTitle;
    
    private Integer totalScore;
    
    private String category;
    
    private String description;
    
    private String recommendation;
    
    private LocalDateTime createdAt;
} 
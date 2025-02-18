package com.work.psychological.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("result_explanations")
public class ResultExplanation {
    
    private Integer assessmentId;
    
    private Integer minScore;
    
    private Integer maxScore;
    
    private String category;
    
    private String description;
    
    private String recommendation;
} 
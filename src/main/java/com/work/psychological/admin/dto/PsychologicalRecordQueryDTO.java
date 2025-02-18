package com.work.psychological.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.work.psychological.common.dto.PageRequestDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PsychologicalRecordQueryDTO extends PageRequestDTO {
    
    /**
     * 姓名(可选，支持模糊查询)
     */
    private String name;
    
    /**
     * 性别(可选，精确匹配)
     * 可选值：MALE, FEMALE, OTHER
     */
    private String gender;
    
    /**
     * 压力水平(可选，精确匹配)
     * 可选值：LOW, MEDIUM, HIGH
     */
    private String stressLevel;
    
    /**
     * 睡眠质量(可选，精确匹配)
     * 可选值：GOOD, NORMAL, POOR
     */
    private String sleepQuality;
    
    /**
     * 情绪状态(可选，支持模糊查询)
     */
    private String emotionalState;
} 
package com.work.psychological.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.work.psychological.common.dto.PageRequestDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAssessmentQueryDTO extends PageRequestDTO {
    
    /**
     * 用户名(可选，支持模糊查询)
     */
    private String username;
    
    /**
     * 问卷标题(可选，支持模糊查询)
     */
    private String assessmentTitle;
} 
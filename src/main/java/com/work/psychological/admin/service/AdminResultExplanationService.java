package com.work.psychological.admin.service;

import com.work.psychological.admin.dto.ResultExplanationCreateDTO;
import com.work.psychological.admin.dto.ResultExplanationUpdateDTO;
import com.work.psychological.admin.dto.ResultExplanationDeleteDTO;
import com.work.psychological.model.entity.ResultExplanation;
import java.util.List;

public interface AdminResultExplanationService {
    
    /**
     * 获取问卷的所有评分规则
     * @param assessmentId 问卷ID
     * @return 评分规则列表
     */
    List<ResultExplanation> listByAssessmentId(Integer assessmentId);
    
    /**
     * 创建评分规则
     * @param createDTO 创建信息
     * @return 创建的评分规则
     */
    ResultExplanation createResultExplanation(ResultExplanationCreateDTO createDTO);
    
    /**
     * 更新评分规则
     * @param updateDTO 更新信息
     * @return 更新后的评分规则
     */
    ResultExplanation updateResultExplanation(ResultExplanationUpdateDTO updateDTO);
    
    /**
     * 删除评分规则
     * @param deleteDTO 删除信息
     */
    void deleteResultExplanation(ResultExplanationDeleteDTO deleteDTO);
} 
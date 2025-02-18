package com.work.psychological.service;

import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.Assessment;

public interface AssessmentValidationService {
    
    /**
     * 验证问卷答案
     * @param assessment 问卷信息
     * @param submitDTO 提交的答案
     * @throws BusinessException 如果验证失败
     */
    void validateAnswers(Assessment assessment, AssessmentSubmitDTO submitDTO);
} 
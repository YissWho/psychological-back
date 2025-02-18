package com.work.psychological.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.vo.StatisticsVO;
import com.work.psychological.admin.dto.AssessmentCreateDTO;
import com.work.psychological.admin.dto.AssessmentUpdateDTO;
import com.work.psychological.admin.dto.AssessmentQueryDTO;
import java.util.List;

public interface AdminAssessmentService {
    
    /**
     * 获取问卷列表（分页）
     * 支持按标题模糊查询
     */
    IPage<Assessment> listAllAssessments(AssessmentQueryDTO queryDTO);
    
    /**
     * 创建新的问卷
     */
    Assessment createAssessment(AssessmentCreateDTO createDTO);
    
    /**
     * 更新问卷信息
     */
    Assessment updateAssessment(AssessmentUpdateDTO updateDTO);
    
    /**
     * 删除问卷
     */
    void deleteAssessment(Integer id);
    
    /**
     * 获取所有问卷的总体统计数据
     */
    StatisticsVO getOverallStatistics();
    
    /**
     * 获取特定问卷的统计数据
     */
    StatisticsVO getAssessmentStatistics(Integer id);
} 
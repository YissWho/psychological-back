package com.work.psychological.service;

import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.UserAssessment;
import com.work.psychological.model.vo.AssessmentResultVO;
import com.work.psychological.model.vo.StatisticsVO;
import java.util.List;

public interface AssessmentService {
    
    /**
     * 获取所有有效的问卷列表
     * @return 问卷列表
     */
    List<Assessment> listActiveAssessments();
    
    /**
     * 获取问卷详情
     * @param id 问卷ID
     * @return 问卷详情
     */
    Assessment getAssessmentById(Integer id);
    
    /**
     * 提交问卷答案
     * @param submitDTO 答案信息
     * @return 测评结果
     */
    UserAssessment submitAssessment(AssessmentSubmitDTO submitDTO);
    
    /**
     * 获取用户的测评历史
     * @return 测评历史列表
     */
    List<UserAssessment> getUserAssessmentHistory();

    /**
     * 获取用户的详细测评结果历史
     * @return 详细测评结果列表
     */
    List<AssessmentResultVO> getUserDetailedAssessmentHistory();

    /**
     * 获取测评统计数据
     * @param assessmentId 问卷ID
     * @return 统计数据
     */
    StatisticsVO getStatistics(Integer assessmentId);
} 
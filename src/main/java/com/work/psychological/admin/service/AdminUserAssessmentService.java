package com.work.psychological.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.admin.dto.UserAssessmentQueryDTO;
import com.work.psychological.model.vo.UserAssessmentDetailVO;

public interface AdminUserAssessmentService {
    
    /**
     * 分页查询用户测评记录
     * @param queryDTO 查询参数(包含分页信息、用户名、问卷标题)
     * @return 测评记录列表
     */
    IPage<UserAssessmentDetailVO> listUserAssessments(UserAssessmentQueryDTO queryDTO);
} 
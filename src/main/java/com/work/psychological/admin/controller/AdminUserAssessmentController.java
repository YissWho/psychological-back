package com.work.psychological.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.common.api.ApiResult;
import com.work.psychological.admin.dto.UserAssessmentQueryDTO;
import com.work.psychological.model.vo.UserAssessmentDetailVO;
import com.work.psychological.admin.service.AdminUserAssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user-assessments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // 只允许管理员访问
public class AdminUserAssessmentController {

    private final AdminUserAssessmentService adminUserAssessmentService;

    /**
     * 分页查询用户测评记录
     * 支持按用户名和问卷标题筛选
     * @param queryDTO 查询参数(包含分页信息、用户名、问卷标题)
     * @return 测评记录列表
     */
    @GetMapping
    public ApiResult<IPage<UserAssessmentDetailVO>> listUserAssessments(@Valid UserAssessmentQueryDTO queryDTO) {
        return ApiResult.success(adminUserAssessmentService.listUserAssessments(queryDTO));
    }
} 
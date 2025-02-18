package com.work.psychological.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.admin.service.AdminAssessmentService;
import com.work.psychological.admin.dto.AssessmentCreateDTO;
import com.work.psychological.admin.dto.AssessmentUpdateDTO;
import com.work.psychological.admin.dto.AssessmentQueryDTO;
import com.work.psychological.model.vo.StatisticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/assessments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // 只允许管理员访问
public class AdminAssessmentController {

    private final AdminAssessmentService adminAssessmentService;

    /**
     * 分页查询问卷列表
     * 支持按标题模糊查询
     */
    @GetMapping
    public ApiResult<IPage<Assessment>> listAllAssessments(@Valid AssessmentQueryDTO queryDTO) {
        return ApiResult.success(adminAssessmentService.listAllAssessments(queryDTO));
    }

    @PostMapping
    public ApiResult<Assessment> createAssessment(@RequestBody @Valid AssessmentCreateDTO createDTO) {
        return ApiResult.success(adminAssessmentService.createAssessment(createDTO));
    }

    @PutMapping
    public ApiResult<Assessment> updateAssessment(@RequestBody @Valid AssessmentUpdateDTO updateDTO) {
        return ApiResult.success(adminAssessmentService.updateAssessment(updateDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteAssessment(@PathVariable Integer id) {
        adminAssessmentService.deleteAssessment(id);
        return ApiResult.success(null);
    }

    @GetMapping("/statistics/overview")
    public ApiResult<StatisticsVO> getOverallStatistics() {
        return ApiResult.success(adminAssessmentService.getOverallStatistics());
    }

    @GetMapping("/{id}/statistics")
    public ApiResult<StatisticsVO> getAssessmentStatistics(@PathVariable Integer id) {
        return ApiResult.success(adminAssessmentService.getAssessmentStatistics(id));
    }
} 
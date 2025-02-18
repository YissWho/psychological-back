package com.work.psychological.controller;

import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.UserAssessment;
import com.work.psychological.model.vo.AssessmentResultVO;
import com.work.psychological.model.vo.StatisticsVO;
import com.work.psychological.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @GetMapping
    public ApiResult<List<Assessment>> listActiveAssessments() {
        return ApiResult.success(assessmentService.listActiveAssessments());
    }

    @GetMapping("/{id}")
    public ApiResult<Assessment> getAssessment(@PathVariable Integer id) {
        return ApiResult.success(assessmentService.getAssessmentById(id));
    }

    @PostMapping("/submit")
    public ApiResult<UserAssessment> submitAssessment(@RequestBody @Valid AssessmentSubmitDTO submitDTO) {
        return ApiResult.success(assessmentService.submitAssessment(submitDTO));
    }

    @GetMapping("/history")
    public ApiResult<List<UserAssessment>> getAssessmentHistory() {
        return ApiResult.success(assessmentService.getUserAssessmentHistory());
    }

    @GetMapping("/history/detailed")
    public ApiResult<List<AssessmentResultVO>> getDetailedAssessmentHistory() {
        return ApiResult.success(assessmentService.getUserDetailedAssessmentHistory());
    }

    @GetMapping("/{id}/statistics")
    public ApiResult<StatisticsVO> getStatistics(@PathVariable Integer id) {
        return ApiResult.success(assessmentService.getStatistics(id));
    }
} 
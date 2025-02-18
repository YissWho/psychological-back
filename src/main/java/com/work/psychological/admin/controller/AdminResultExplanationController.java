package com.work.psychological.admin.controller;

import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.entity.ResultExplanation;
import com.work.psychological.admin.dto.ResultExplanationCreateDTO;
import com.work.psychological.admin.dto.ResultExplanationUpdateDTO;
import com.work.psychological.admin.dto.ResultExplanationDeleteDTO;
import com.work.psychological.admin.service.AdminResultExplanationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/result-explanations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // 只允许管理员访问
public class AdminResultExplanationController {

    private final AdminResultExplanationService adminResultExplanationService;

    @GetMapping("/assessment/{assessmentId}")
    public ApiResult<List<ResultExplanation>> listByAssessmentId(@PathVariable Integer assessmentId) {
        return ApiResult.success(adminResultExplanationService.listByAssessmentId(assessmentId));
    }

    @PostMapping
    public ApiResult<ResultExplanation> createResultExplanation(@RequestBody @Valid ResultExplanationCreateDTO createDTO) {
        return ApiResult.success(adminResultExplanationService.createResultExplanation(createDTO));
    }

    @PutMapping
    public ApiResult<ResultExplanation> updateResultExplanation(@RequestBody @Valid ResultExplanationUpdateDTO updateDTO) {
        return ApiResult.success(adminResultExplanationService.updateResultExplanation(updateDTO));
    }

    @DeleteMapping
    public ApiResult<Void> deleteResultExplanation(@RequestBody @Valid ResultExplanationDeleteDTO deleteDTO) {
        adminResultExplanationService.deleteResultExplanation(deleteDTO);
        return ApiResult.success(null);
    }
} 
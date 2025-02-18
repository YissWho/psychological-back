package com.work.psychological.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.common.api.ApiResult;
import com.work.psychological.admin.dto.PsychologicalRecordQueryDTO;
import com.work.psychological.model.entity.PsychologicalRecord;
import com.work.psychological.admin.service.AdminPsychologicalRecordService;
import com.work.psychological.admin.vo.AdminStatisticsVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/psychological-records")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")  // 只允许管理员访问
public class AdminPsychologicalRecordController {

    private final AdminPsychologicalRecordService adminPsychologicalRecordService;

    /**
     * 分页查询心理档案
     * 支持按姓名、性别、压力水平、睡眠质量、情绪状态筛选
     * @param queryDTO 查询参数
     * @return 心理档案列表
     */
    @GetMapping
    public ApiResult<IPage<PsychologicalRecord>> listPsychologicalRecords(@Valid PsychologicalRecordQueryDTO queryDTO) {
        return ApiResult.success(adminPsychologicalRecordService.listPsychologicalRecords(queryDTO));
    }

    /**
     * 获取统计数据
     * @return 统计图表数据
     */
    @GetMapping("/statistics")
    public ApiResult<AdminStatisticsVO> getStatistics() {
        return ApiResult.success(adminPsychologicalRecordService.getStatistics());
    }
} 
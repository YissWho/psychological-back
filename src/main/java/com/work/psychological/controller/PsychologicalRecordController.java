package com.work.psychological.controller;

import com.work.psychological.common.api.ApiResult;
import com.work.psychological.model.dto.PsychologicalRecordDTO;
import com.work.psychological.model.entity.PsychologicalRecord;
import com.work.psychological.service.PsychologicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psychological-records")
@RequiredArgsConstructor
public class PsychologicalRecordController {

    private final PsychologicalRecordService psychologicalRecordService;

    @GetMapping("/current")
    public ApiResult<PsychologicalRecord> getCurrentUserRecord() {
        return ApiResult.success(psychologicalRecordService.getCurrentUserRecord());
    }

    @PutMapping("/current")
    public ApiResult<PsychologicalRecord> updateCurrentUserRecord(@RequestBody @Valid PsychologicalRecordDTO recordDTO) {
        return ApiResult.success(psychologicalRecordService.updateCurrentUserRecord(recordDTO));
    }
} 
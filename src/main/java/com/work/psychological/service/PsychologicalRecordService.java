package com.work.psychological.service;

import com.work.psychological.model.dto.PsychologicalRecordDTO;
import com.work.psychological.model.entity.PsychologicalRecord;

public interface PsychologicalRecordService {
    
    /**
     * 获取当前用户的心理档案
     * @return 心理档案信息
     */
    PsychologicalRecord getCurrentUserRecord();
    
    /**
     * 更新当前用户的心理档案
     * @param recordDTO 心理档案信息
     * @return 更新后的心理档案
     */
    PsychologicalRecord updateCurrentUserRecord(PsychologicalRecordDTO recordDTO);
} 
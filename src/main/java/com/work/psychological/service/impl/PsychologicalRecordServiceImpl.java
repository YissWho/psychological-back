package com.work.psychological.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.PsychologicalRecordMapper;
import com.work.psychological.model.dto.PsychologicalRecordDTO;
import com.work.psychological.model.entity.PsychologicalRecord;
import com.work.psychological.model.entity.User;
import com.work.psychological.service.PsychologicalRecordService;
import com.work.psychological.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PsychologicalRecordServiceImpl extends ServiceImpl<PsychologicalRecordMapper, PsychologicalRecord> 
        implements PsychologicalRecordService {

    private final UserService userService;

    @Override
    public PsychologicalRecord getCurrentUserRecord() {
        User currentUser = userService.getCurrentUser();
        
        LambdaQueryWrapper<PsychologicalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PsychologicalRecord::getUserId, currentUser.getId());
        
        PsychologicalRecord record = this.getOne(queryWrapper);
        if (record == null) {
            throw new BusinessException("当前用户暂无心理档案");
        }
        
        return record;
    }

    @Override
    @Transactional
    public PsychologicalRecord updateCurrentUserRecord(PsychologicalRecordDTO recordDTO) {
        User currentUser = userService.getCurrentUser();
        
        // 查找当前用户的心理档案
        LambdaQueryWrapper<PsychologicalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PsychologicalRecord::getUserId, currentUser.getId());
        PsychologicalRecord record = this.getOne(queryWrapper);
        
        // 如果不存在则创建新的档案
        if (record == null) {
            record = new PsychologicalRecord();
            record.setUserId(currentUser.getId());
        }
        
        // 更新档案信息
        BeanUtils.copyProperties(recordDTO, record);
        
        // 保存或更新档案
        this.saveOrUpdate(record);
        
        log.info("用户 {} 的心理档案已更新", currentUser.getUsername());
        return record;
    }
} 
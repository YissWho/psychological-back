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
import org.springframework.util.StringUtils;
import com.work.psychological.mapper.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PsychologicalRecordServiceImpl extends ServiceImpl<PsychologicalRecordMapper, PsychologicalRecord> 
        implements PsychologicalRecordService {

    private final UserService userService;
    private final PsychologicalRecordMapper psychologicalRecordMapper;
    private final UserMapper userMapper;

    @Override
    public PsychologicalRecord getCurrentUserRecord() {
        User currentUser = userService.getCurrentUser();
        
        LambdaQueryWrapper<PsychologicalRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PsychologicalRecord::getUserId, currentUser.getId());
        
        PsychologicalRecord record = this.getOne(queryWrapper);
        if (record == null) {
            throw new BusinessException("当前用户暂无心理档案");
        }
        
        // 设置用户头像
        record.setAvatar(currentUser.getAvatar());
        
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
        
        // 如果提供了头像URL，更新用户头像
        if (StringUtils.hasText(recordDTO.getAvatar())) {
            User user = new User();
            user.setId(currentUser.getId());
            user.setAvatar(recordDTO.getAvatar());
            userMapper.updateById(user);
            log.info("用户 {} 的头像已更新", currentUser.getUsername());
        }
        
        log.info("用户 {} 的心理档案已更新", currentUser.getUsername());
        
        // 重新查询用户信息，获取最新的头像URL
        User updatedUser = userMapper.selectById(currentUser.getId());
        record.setAvatar(updatedUser.getAvatar());
        
        return record;
    }
} 
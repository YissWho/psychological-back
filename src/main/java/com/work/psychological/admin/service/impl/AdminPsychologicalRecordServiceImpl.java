package com.work.psychological.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.work.psychological.admin.dto.PsychologicalRecordQueryDTO;
import com.work.psychological.admin.service.AdminPsychologicalRecordService;
import com.work.psychological.admin.vo.ChartDataVO;
import com.work.psychological.admin.vo.AdminStatisticsVO;
import com.work.psychological.mapper.PsychologicalRecordMapper;
import com.work.psychological.mapper.AssessmentMapper;
import com.work.psychological.mapper.UserAssessmentMapper;
import com.work.psychological.model.entity.PsychologicalRecord;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.UserAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPsychologicalRecordServiceImpl implements AdminPsychologicalRecordService {

    private final PsychologicalRecordMapper psychologicalRecordMapper;
    private final AssessmentMapper assessmentMapper;
    private final UserAssessmentMapper userAssessmentMapper;

    @Override
    public IPage<PsychologicalRecord> listPsychologicalRecords(PsychologicalRecordQueryDTO queryDTO) {
        // 创建分页对象
        Page<PsychologicalRecord> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<PsychologicalRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        // 姓名模糊查询
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(PsychologicalRecord::getName, queryDTO.getName());
        }
        
        // 性别精确匹配
        if (StringUtils.hasText(queryDTO.getGender())) {
            queryWrapper.eq(PsychologicalRecord::getGender, queryDTO.getGender());
        }
        
        // 压力水平精确匹配
        if (StringUtils.hasText(queryDTO.getStressLevel())) {
            queryWrapper.eq(PsychologicalRecord::getStressLevel, queryDTO.getStressLevel());
        }
        
        // 睡眠质量精确匹配
        if (StringUtils.hasText(queryDTO.getSleepQuality())) {
            queryWrapper.eq(PsychologicalRecord::getSleepQuality, queryDTO.getSleepQuality());
        }
        
        // 情绪状态模糊查询
        if (StringUtils.hasText(queryDTO.getEmotionalState())) {
            queryWrapper.like(PsychologicalRecord::getEmotionalState, queryDTO.getEmotionalState());
        }
        
        // 只查询未删除的记录
        queryWrapper.eq(PsychologicalRecord::getIsDeleted, 0);
        
        // 按更新时间倒序排序
        queryWrapper.orderByDesc(PsychologicalRecord::getUpdatedAt);
        
        // 执行分页查询
        return psychologicalRecordMapper.selectPage(page, queryWrapper);
    }

    @Override
    public AdminStatisticsVO getStatistics() {
        AdminStatisticsVO statisticsVO = new AdminStatisticsVO();
        List<ChartDataVO> charts = new ArrayList<>();
        
        // 1. 性别分布（饼图）
        ChartDataVO genderChart = new ChartDataVO();
        genderChart.setTitle("性别分布");
        genderChart.setType("pie");
        
        LambdaQueryWrapper<PsychologicalRecord> genderWrapper = new LambdaQueryWrapper<>();
        genderWrapper.eq(PsychologicalRecord::getIsDeleted, 0);
        List<PsychologicalRecord> allRecords = psychologicalRecordMapper.selectList(genderWrapper);
        
        Map<String, Long> genderCount = allRecords.stream()
                .collect(Collectors.groupingBy(
                    PsychologicalRecord::getGender,
                    Collectors.counting()
                ));
        
        List<ChartDataVO.ChartItemVO> genderData = new ArrayList<>();
        genderCount.forEach((gender, count) -> {
            String label = switch (gender) {
                case "MALE" -> "男性";
                case "FEMALE" -> "女性";
                default -> "其他";
            };
            genderData.add(new ChartDataVO.ChartItemVO(label, count.intValue()));
        });
        genderChart.setData(genderData);
        charts.add(genderChart);
        
        // 2. 压力水平分布（柱状图）
        ChartDataVO stressChart = new ChartDataVO();
        stressChart.setTitle("压力水平分布");
        stressChart.setType("bar");
        
        Map<String, Long> stressCount = allRecords.stream()
                .filter(r -> r.getStressLevel() != null)
                .collect(Collectors.groupingBy(
                    PsychologicalRecord::getStressLevel,
                    Collectors.counting()
                ));
        
        List<ChartDataVO.ChartItemVO> stressData = new ArrayList<>();
        stressCount.forEach((level, count) -> {
            String label = switch (level) {
                case "LOW" -> "低压力";
                case "MEDIUM" -> "中等压力";
                case "HIGH" -> "高压力";
                default -> level;
            };
            stressData.add(new ChartDataVO.ChartItemVO(label, count.intValue()));
        });
        stressChart.setData(stressData);
        charts.add(stressChart);
        
        // 3. 问卷填写人数统计（柱状图）
        ChartDataVO assessmentChart = new ChartDataVO();
        assessmentChart.setTitle("问卷填写人数统计");
        assessmentChart.setType("bar");
        
        // 查询所有有效的问卷
        LambdaQueryWrapper<Assessment> assessmentWrapper = new LambdaQueryWrapper<>();
        assessmentWrapper.eq(Assessment::getIsActive, 1);
        List<Assessment> assessments = assessmentMapper.selectList(assessmentWrapper);
        
        List<ChartDataVO.ChartItemVO> assessmentData = new ArrayList<>();
        for (Assessment assessment : assessments) {
            // 统计每个问卷的填写次数
            LambdaQueryWrapper<UserAssessment> countWrapper = new LambdaQueryWrapper<>();
            countWrapper.eq(UserAssessment::getAssessmentId, assessment.getId());
            long count = userAssessmentMapper.selectCount(countWrapper);
            
            assessmentData.add(new ChartDataVO.ChartItemVO(
                assessment.getTitle(),
                (int) count
            ));
        }
        
        assessmentChart.setData(assessmentData);
        charts.add(assessmentChart);
        
        statisticsVO.setCharts(charts);
        return statisticsVO;
    }
} 
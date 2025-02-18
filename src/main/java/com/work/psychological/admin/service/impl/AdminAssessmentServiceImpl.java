package com.work.psychological.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.psychological.admin.service.AdminAssessmentService;
import com.work.psychological.admin.dto.AssessmentCreateDTO;
import com.work.psychological.admin.dto.AssessmentUpdateDTO;
import com.work.psychological.admin.dto.AssessmentQueryDTO;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.AssessmentMapper;
import com.work.psychological.mapper.UserAssessmentMapper;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.UserAssessment;
import com.work.psychological.model.vo.ChartDataVO;
import com.work.psychological.model.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAssessmentServiceImpl implements AdminAssessmentService {

    private final AssessmentMapper assessmentMapper;
    private final UserAssessmentMapper userAssessmentMapper;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<Assessment> listAllAssessments(AssessmentQueryDTO queryDTO) {
        // 创建分页对象
        Page<Assessment> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 创建查询条件
        LambdaQueryWrapper<Assessment> queryWrapper = new LambdaQueryWrapper<>();
        
        // 标题模糊查询
        if (StringUtils.hasText(queryDTO.getTitle())) {
            queryWrapper.like(Assessment::getTitle, queryDTO.getTitle());
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Assessment::getCreatedAt);
        
        // 执行分页查询
        return assessmentMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public Assessment createAssessment(AssessmentCreateDTO createDTO) {
        try {
            Assessment assessment = new Assessment();
            BeanUtils.copyProperties(createDTO, assessment);
            
            // 将questionnaire对象转换为JSON字符串
            String questionnaireJson = objectMapper.writeValueAsString(createDTO.getQuestionnaire());
            assessment.setQuestionnaire(questionnaireJson);
            
            // 设置默认值
            assessment.setIsActive(1);
            assessmentMapper.insert(assessment);
            
            return assessment;
        } catch (JsonProcessingException e) {
            log.error("问卷JSON转换失败", e);
            throw new BusinessException("问卷格式错误");
        }
    }

    @Override
    @Transactional
    public Assessment updateAssessment(AssessmentUpdateDTO updateDTO) {
        try {
            // 检查问卷是否存在
            Assessment existingAssessment = assessmentMapper.selectById(updateDTO.getId());
            if (existingAssessment == null) {
                throw new BusinessException("问卷不存在");
            }

            Assessment assessment = new Assessment();
            BeanUtils.copyProperties(updateDTO, assessment);
            
            // 将questionnaire对象转换为JSON字符串
            String questionnaireJson = objectMapper.writeValueAsString(updateDTO.getQuestionnaire());
            assessment.setQuestionnaire(questionnaireJson);
            
            assessmentMapper.updateById(assessment);
            return assessment;
            
        } catch (JsonProcessingException e) {
            log.error("问卷JSON转换失败", e);
            throw new BusinessException("问卷格式错误");
        }
    }

    @Override
    @Transactional
    public void deleteAssessment(Integer id) {
        Assessment assessment = assessmentMapper.selectById(id);
        if (assessment == null) {
            throw new BusinessException("问卷不存在");
        }
        // 检查是否有用户已完成该问卷
        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAssessment::getAssessmentId, id);
        long count = userAssessmentMapper.selectCount(queryWrapper);
        if (count > 0) {
            // 如果有用户完成了问卷，则只做逻辑删除（设置为未激活）
            assessment.setIsActive(0);
            assessmentMapper.updateById(assessment);
        } else {
            // 如果没有用户完成问卷，则可以物理删除
            assessmentMapper.deleteById(id);
        }
    }

    @Override
    public StatisticsVO getOverallStatistics() {
        StatisticsVO statisticsVO = new StatisticsVO();
        List<ChartDataVO> charts = new ArrayList<>();

        // 1. 各问卷完成人数统计
        ChartDataVO completionChart = new ChartDataVO();
        completionChart.setTitle("问卷完成情况统计");

        // 获取所有问卷
        List<Assessment> assessments = assessmentMapper.selectList(null);
        List<ChartDataVO.ChartItemVO> completionData = new ArrayList<>();

        for (Assessment assessment : assessments) {
            LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserAssessment::getAssessmentId, assessment.getId());
            long count = userAssessmentMapper.selectCount(queryWrapper);
            
            completionData.add(new ChartDataVO.ChartItemVO(
                assessment.getTitle(),
                (int) count
            ));
        }
        completionChart.setData(completionData);
        charts.add(completionChart);

        // 2. 总体结果分布统计
        ChartDataVO resultChart = new ChartDataVO();
        resultChart.setTitle("总体结果分布");

        List<UserAssessment> allAssessments = userAssessmentMapper.selectList(null);
        Map<String, Long> resultCount = allAssessments.stream()
                .collect(Collectors.groupingBy(
                    UserAssessment::getResultCategory,
                    Collectors.counting()
                ));

        List<ChartDataVO.ChartItemVO> resultData = resultCount.entrySet().stream()
                .map(entry -> new ChartDataVO.ChartItemVO(
                    entry.getKey(),
                    entry.getValue().intValue()
                ))
                .collect(Collectors.toList());

        resultChart.setData(resultData);
        charts.add(resultChart);

        statisticsVO.setCharts(charts);
        return statisticsVO;
    }

    @Override
    public StatisticsVO getAssessmentStatistics(Integer id) {
        // 检查问卷是否存在
        Assessment assessment = assessmentMapper.selectById(id);
        if (assessment == null) {
            throw new BusinessException("问卷不存在");
        }

        StatisticsVO statisticsVO = new StatisticsVO();
        List<ChartDataVO> charts = new ArrayList<>();

        // 1. 结果分布统计
        ChartDataVO resultChart = new ChartDataVO();
        resultChart.setTitle(assessment.getTitle() + " - 结果分布");

        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAssessment::getAssessmentId, id);
        List<UserAssessment> assessments = userAssessmentMapper.selectList(queryWrapper);

        Map<String, Long> resultCount = assessments.stream()
                .collect(Collectors.groupingBy(
                    UserAssessment::getResultCategory,
                    Collectors.counting()
                ));

        List<ChartDataVO.ChartItemVO> resultData = resultCount.entrySet().stream()
                .map(entry -> new ChartDataVO.ChartItemVO(
                    entry.getKey(),
                    entry.getValue().intValue()
                ))
                .collect(Collectors.toList());

        resultChart.setData(resultData);
        charts.add(resultChart);

        // 2. 得分区间分布
        ChartDataVO scoreChart = new ChartDataVO();
        scoreChart.setTitle(assessment.getTitle() + " - 得分分布");

        int[] scoreRanges = {0, 5, 10, 15, 20, 27};
        List<ChartDataVO.ChartItemVO> scoreData = new ArrayList<>();

        for (int i = 0; i < scoreRanges.length - 1; i++) {
            int min = scoreRanges[i];
            int max = scoreRanges[i + 1];
            int count = (int) assessments.stream()
                    .filter(a -> a.getTotalScore() >= min && a.getTotalScore() < max)
                    .count();

            scoreData.add(new ChartDataVO.ChartItemVO(
                min + "-" + max + "分",
                count
            ));
        }

        scoreChart.setData(scoreData);
        charts.add(scoreChart);

        statisticsVO.setCharts(charts);
        return statisticsVO;
    }
} 
package com.work.psychological.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.AssessmentMapper;
import com.work.psychological.mapper.ResultExplanationMapper;
import com.work.psychological.mapper.UserAssessmentMapper;
import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.ResultExplanation;
import com.work.psychological.model.entity.User;
import com.work.psychological.model.entity.UserAssessment;
import com.work.psychological.model.vo.AssessmentResultVO;
import com.work.psychological.model.vo.ChartDataVO;
import com.work.psychological.model.vo.StatisticsVO;
import com.work.psychological.service.AssessmentService;
import com.work.psychological.service.AssessmentValidationService;
import com.work.psychological.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl extends ServiceImpl<AssessmentMapper, Assessment> implements AssessmentService {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final UserAssessmentMapper userAssessmentMapper;
    private final ResultExplanationMapper resultExplanationMapper;
    private final AssessmentMapper assessmentMapper;
    private final AssessmentValidationService assessmentValidationService;

    @Override
    public List<Assessment> listActiveAssessments() {
        LambdaQueryWrapper<Assessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Assessment::getIsActive, 1)
                   .orderByDesc(Assessment::getCreatedAt);
        return this.list(queryWrapper);
    }

    @Override
    public Assessment getAssessmentById(Integer id) {
        Assessment assessment = this.getById(id);
        if (assessment == null || assessment.getIsActive() != 1) {
            throw new BusinessException("问卷不存在或已停用");
        }
        return assessment;
    }

    @Override
    @Transactional
    public UserAssessment submitAssessment(AssessmentSubmitDTO submitDTO) {
        // 获取当前用户
        User currentUser = userService.getCurrentUser();
        
        // 获取问卷信息
        Assessment assessment = this.getAssessmentById(submitDTO.getAssessmentId());
        
        // 验证答案
        assessmentValidationService.validateAnswers(assessment, submitDTO);
        
        try {
            // 解析问卷JSON
            JsonNode questionnaireNode = objectMapper.readTree(assessment.getQuestionnaire());
            
            // 创建选项ID到分数的映射
            Map<Integer, Integer> optionScores = createOptionScoreMap(questionnaireNode);
            
            // 计算总分
            int totalScore = calculateTotalScore(submitDTO.getAnswers(), optionScores);
            
            // 获取结果解释
            ResultExplanation resultExplanation = getResultExplanation(assessment.getId(), totalScore);
            if (resultExplanation == null) {
                throw new BusinessException("无法获取测评结果解释");
            }
            
            // 保存用户答案
            UserAssessment userAssessment = new UserAssessment();
            userAssessment.setUserId(currentUser.getId());
            userAssessment.setAssessmentId(submitDTO.getAssessmentId());
            userAssessment.setAnswers(objectMapper.writeValueAsString(submitDTO.getAnswers()));
            userAssessment.setTotalScore(totalScore);
            userAssessment.setResultCategory(resultExplanation.getCategory());
            
            userAssessmentMapper.insert(userAssessment);
            
            log.info("用户 {} 完成问卷 {}, 得分: {}, 结果: {}", 
                    currentUser.getUsername(), assessment.getTitle(), 
                    totalScore, resultExplanation.getCategory());
            return userAssessment;
            
        } catch (JsonProcessingException e) {
            log.error("解析问卷JSON失败", e);
            throw new BusinessException("提交答案失败");
        }
    }

    @Override
    public List<UserAssessment> getUserAssessmentHistory() {
        User currentUser = userService.getCurrentUser();
        
        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAssessment::getUserId, currentUser.getId())
                   .orderByDesc(UserAssessment::getCreatedAt);
        
        List<UserAssessment> userAssessments = userAssessmentMapper.selectList(queryWrapper);
        
        // 如果没有测评记录，直接返回空列表
        if (userAssessments.isEmpty()) {
            return userAssessments;
        }
        
        // 获取所有相关的问卷ID
        List<Integer> assessmentIds = userAssessments.stream()
                .map(UserAssessment::getAssessmentId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询问卷信息
        LambdaQueryWrapper<Assessment> assessmentWrapper = new LambdaQueryWrapper<>();
        assessmentWrapper.in(Assessment::getId, assessmentIds);
        List<Assessment> assessments = assessmentMapper.selectList(assessmentWrapper);
        
        // 创建问卷ID到标题的映射
        Map<Integer, String> assessmentTitleMap = assessments.stream()
                .collect(Collectors.toMap(
                    Assessment::getId,
                    Assessment::getTitle
                ));
        
        // 为每个记录设置问卷标题，并清除answers字段
        userAssessments.forEach(ua -> {
            ua.setAnswers(null);  // 清除answers字段
            // 使用反射设置assessmentTitle字段
            try {
                java.lang.reflect.Field titleField = ua.getClass().getDeclaredField("assessmentTitle");
                titleField.setAccessible(true);
                titleField.set(ua, assessmentTitleMap.get(ua.getAssessmentId()));
            } catch (Exception e) {
                log.error("设置问卷标题失败", e);
            }
        });
        
        return userAssessments;
    }

    @Override
    public List<AssessmentResultVO> getUserDetailedAssessmentHistory() {
        User currentUser = userService.getCurrentUser();
        
        // 获取用户的所有测评记录
        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAssessment::getUserId, currentUser.getId())
                   .orderByDesc(UserAssessment::getCreatedAt);
        List<UserAssessment> userAssessments = userAssessmentMapper.selectList(queryWrapper);
        
        List<AssessmentResultVO> resultVOs = new ArrayList<>();
        
        for (UserAssessment userAssessment : userAssessments) {
            AssessmentResultVO resultVO = new AssessmentResultVO();
            resultVO.setId(userAssessment.getId());
            resultVO.setAssessmentId(userAssessment.getAssessmentId());
            resultVO.setTotalScore(userAssessment.getTotalScore());
            resultVO.setCreatedAt(userAssessment.getCreatedAt());
            
            // 获取问卷信息
            Assessment assessment = this.getById(userAssessment.getAssessmentId());
            if (assessment != null) {
                resultVO.setAssessmentTitle(assessment.getTitle());
            }
            
            // 获取结果解释
            ResultExplanation explanation = getResultExplanation(
                userAssessment.getAssessmentId(), 
                userAssessment.getTotalScore()
            );
            
            if (explanation != null) {
                resultVO.setCategory(explanation.getCategory());
                resultVO.setDescription(explanation.getDescription());
                resultVO.setRecommendation(explanation.getRecommendation());
            }
            
            resultVOs.add(resultVO);
        }
        
        return resultVOs;
    }

    private Map<Integer, Integer> createOptionScoreMap(JsonNode questionnaireNode) {
        Map<Integer, Integer> optionScores = new HashMap<>();
        JsonNode questionsNode = questionnaireNode.get("questions");
        
        for (JsonNode questionNode : questionsNode) {
            JsonNode optionsNode = questionNode.get("options");
            for (JsonNode optionNode : optionsNode) {
                optionScores.put(
                    optionNode.get("id").asInt(),
                    optionNode.get("score").asInt()
                );
            }
        }
        
        return optionScores;
    }

    private int calculateTotalScore(List<AssessmentSubmitDTO.Answer> answers, Map<Integer, Integer> optionScores) {
        return answers.stream()
                .mapToInt(answer -> optionScores.getOrDefault(answer.getOptionId(), 0))
                .sum();
    }

    private ResultExplanation getResultExplanation(Integer assessmentId, int totalScore) {
        LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResultExplanation::getAssessmentId, assessmentId)
                   .le(ResultExplanation::getMinScore, totalScore)
                   .ge(ResultExplanation::getMaxScore, totalScore);
        
        return resultExplanationMapper.selectOne(queryWrapper);
    }

    @Override
    public StatisticsVO getStatistics(Integer assessmentId) {
        // 获取所有相关的测评记录
        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAssessment::getAssessmentId, assessmentId);
        List<UserAssessment> assessments = userAssessmentMapper.selectList(queryWrapper);

        // 创建返回对象
        StatisticsVO statisticsVO = new StatisticsVO();
        List<ChartDataVO> charts = new ArrayList<>();

        // 不同程度的分布
        ChartDataVO categoryChart = new ChartDataVO();
        categoryChart.setTitle("不同程度的分布");
        
        Map<String, Long> categoryCount = assessments.stream()
                .collect(Collectors.groupingBy(
                    UserAssessment::getResultCategory,
                    Collectors.counting()
                ));

        List<ChartDataVO.ChartItemVO> categoryData = categoryCount.entrySet().stream()
                .map(entry -> new ChartDataVO.ChartItemVO(
                    entry.getKey(),
                    entry.getValue().intValue()
                ))
                .collect(Collectors.toList());

        categoryChart.setData(categoryData);
        charts.add(categoryChart);

        // 2. 得分区间分布
        ChartDataVO scoreChart = new ChartDataVO();
        scoreChart.setTitle("得分区间分布");
        
        // 定义得分区间
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
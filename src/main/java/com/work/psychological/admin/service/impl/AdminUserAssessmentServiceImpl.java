package com.work.psychological.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.psychological.admin.dto.UserAssessmentQueryDTO;
import com.work.psychological.admin.service.AdminUserAssessmentService;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.*;
import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.*;
import com.work.psychological.model.vo.UserAssessmentDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserAssessmentServiceImpl implements AdminUserAssessmentService {

    private final UserAssessmentMapper userAssessmentMapper;
    private final UserMapper userMapper;
    private final AssessmentMapper assessmentMapper;
    private final ResultExplanationMapper resultExplanationMapper;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<UserAssessmentDetailVO> listUserAssessments(UserAssessmentQueryDTO queryDTO) {
        // 创建分页对象
        Page<UserAssessment> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<UserAssessment> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果指定了用户名，先查询用户ID
        if (StringUtils.hasText(queryDTO.getUsername())) {
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.like(User::getUsername, queryDTO.getUsername());
            List<User> users = userMapper.selectList(userWrapper);
            if (!users.isEmpty()) {
                queryWrapper.in(UserAssessment::getUserId, users.stream().map(User::getId).toList());
            } else {
                // 如果没有找到匹配的用户，直接返回空结果
                return new Page<>();
            }
        }
        
        // 如果指定了问卷标题，先查询问卷ID
        if (StringUtils.hasText(queryDTO.getAssessmentTitle())) {
            LambdaQueryWrapper<Assessment> assessmentWrapper = new LambdaQueryWrapper<>();
            assessmentWrapper.like(Assessment::getTitle, queryDTO.getAssessmentTitle());
            List<Assessment> assessments = assessmentMapper.selectList(assessmentWrapper);
            if (!assessments.isEmpty()) {
                queryWrapper.in(UserAssessment::getAssessmentId, assessments.stream().map(Assessment::getId).toList());
            } else {
                // 如果没有找到匹配的问卷，直接返回空结果
                return new Page<>();
            }
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(UserAssessment::getCreatedAt);
        
        // 执行分页查询
        IPage<UserAssessment> assessmentPage = userAssessmentMapper.selectPage(page, queryWrapper);
        
        // 转换为详情VO
        return assessmentPage.convert(this::convertToDetailVO);
    }

    private UserAssessmentDetailVO convertToDetailVO(UserAssessment assessment) {
        try {
            UserAssessmentDetailVO vo = new UserAssessmentDetailVO();
            
            // 复制基本信息
            vo.setId(assessment.getId());
            vo.setUserId(assessment.getUserId());
            vo.setAssessmentId(assessment.getAssessmentId());
            vo.setTotalScore(assessment.getTotalScore());
            vo.setResultCategory(assessment.getResultCategory());
            vo.setCreatedAt(assessment.getCreatedAt());
            
            // 获取用户信息
            User user = userMapper.selectById(assessment.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
            }
            
            // 获取问卷信息
            Assessment questionnaire = assessmentMapper.selectById(assessment.getAssessmentId());
            if (questionnaire != null) {
                vo.setAssessmentTitle(questionnaire.getTitle());
                
                // 解析问卷内容，构建问题和选项的映射
                JsonNode questionnaireNode = objectMapper.readTree(questionnaire.getQuestionnaire());
                JsonNode questionsNode = questionnaireNode != null ? questionnaireNode.get("questions") : null;
                
                if (questionsNode != null && questionsNode.isArray()) {
                    Map<Integer, JsonNode> questionMap = new HashMap<>();
                    Map<Integer, JsonNode> optionMap = new HashMap<>();
                    
                    for (JsonNode questionNode : questionsNode) {
                        if (questionNode.has("id")) {
                            int questionId = questionNode.get("id").asInt();
                            questionMap.put(questionId, questionNode);
                            
                            JsonNode optionsNode = questionNode.get("options");
                            if (optionsNode != null && optionsNode.isArray()) {
                                for (JsonNode optionNode : optionsNode) {
                                    if (optionNode.has("id")) {
                                        int optionId = optionNode.get("id").asInt();
                                        optionMap.put(optionId, optionNode);
                                    }
                                }
                            }
                        }
                    }
                    
                    // 解析用户答案
                    if (StringUtils.hasText(assessment.getAnswers())) {
                        List<AssessmentSubmitDTO.Answer> answers = objectMapper.readValue(
                            assessment.getAnswers(),
                            new TypeReference<List<AssessmentSubmitDTO.Answer>>() {}
                        );
                        
                        // 构建答案详情
                        List<UserAssessmentDetailVO.AnswerDetail> answerDetails = new ArrayList<>();
                        for (AssessmentSubmitDTO.Answer answer : answers) {
                            UserAssessmentDetailVO.AnswerDetail detail = new UserAssessmentDetailVO.AnswerDetail();
                            detail.setQuestionId(answer.getQuestionId());
                            detail.setOptionId(answer.getOptionId());
                            
                            // 设置问题文本
                            JsonNode questionNode = questionMap.get(answer.getQuestionId());
                            if (questionNode != null && questionNode.has("text")) {
                                detail.setQuestionText(questionNode.get("text").asText());
                            }
                            
                            // 设置选项文本和分数
                            JsonNode optionNode = optionMap.get(answer.getOptionId());
                            if (optionNode != null) {
                                if (optionNode.has("text")) {
                                    detail.setOptionText(optionNode.get("text").asText());
                                }
                                if (optionNode.has("score")) {
                                    detail.setScore(optionNode.get("score").asInt());
                                }
                            }
                            
                            answerDetails.add(detail);
                        }
                        vo.setAnswers(answerDetails);
                    }
                }
            }
            
            // 获取结果解释
            LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ResultExplanation::getAssessmentId, assessment.getAssessmentId())
                       .eq(ResultExplanation::getCategory, assessment.getResultCategory());
            ResultExplanation explanation = resultExplanationMapper.selectOne(queryWrapper);
            
            if (explanation != null) {
                vo.setResultDescription(explanation.getDescription());
                vo.setRecommendation(explanation.getRecommendation());
            }
            
            return vo;
            
        } catch (JsonProcessingException e) {
            log.error("解析测评记录失败", e);
            throw new BusinessException("解析测评记录失败");
        }
    }
} 
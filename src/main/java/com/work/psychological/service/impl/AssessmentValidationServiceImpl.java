package com.work.psychological.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.model.dto.AssessmentSubmitDTO;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.service.AssessmentValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssessmentValidationServiceImpl implements AssessmentValidationService {

    private final ObjectMapper objectMapper;

    @Override
    public void validateAnswers(Assessment assessment, AssessmentSubmitDTO submitDTO) {
        try {
            JsonNode questionnaireNode = objectMapper.readTree(assessment.getQuestionnaire());
            JsonNode questionsNode = questionnaireNode.get("questions");
            
            if (questionsNode == null || !questionsNode.isArray()) {
                throw new BusinessException("问卷格式错误");
            }

            // 1. 获取所有问题ID和每个问题的选项ID
            Map<Integer, Set<Integer>> questionOptions = new HashMap<>();
            Set<Integer> allQuestionIds = new HashSet<>();
            
            for (JsonNode questionNode : questionsNode) {
                int questionId = questionNode.get("id").asInt();
                allQuestionIds.add(questionId);
                
                Set<Integer> optionIds = new HashSet<>();
                JsonNode optionsNode = questionNode.get("options");
                for (JsonNode optionNode : optionsNode) {
                    optionIds.add(optionNode.get("id").asInt());
                }
                questionOptions.put(questionId, optionIds);
            }

            // 2. 检查是否有重复的问题ID
            Set<Integer> answeredQuestionIds = new HashSet<>();
            for (AssessmentSubmitDTO.Answer answer : submitDTO.getAnswers()) {
                if (!answeredQuestionIds.add(answer.getQuestionId())) {
                    throw new BusinessException(String.format("问题ID %d 重复回答", answer.getQuestionId()));
                }
            }

            // 3. 验证每个答案
            for (AssessmentSubmitDTO.Answer answer : submitDTO.getAnswers()) {
                // 检查问题ID是否存在
                if (!questionOptions.containsKey(answer.getQuestionId())) {
                    throw new BusinessException(String.format("问题ID %d 不存在", answer.getQuestionId()));
                }

                // 检查选项ID是否存在于对应的问题中
                Set<Integer> validOptionIds = questionOptions.get(answer.getQuestionId());
                if (!validOptionIds.contains(answer.getOptionId())) {
                    throw new BusinessException(
                        String.format("问题ID %d 不存在选项ID %d", 
                            answer.getQuestionId(), answer.getOptionId())
                    );
                }
            }

            // 4. 检查是否所有问题都已回答
            if (answeredQuestionIds.size() != allQuestionIds.size()) {
                Set<Integer> unansweredQuestions = new HashSet<>(allQuestionIds);
                unansweredQuestions.removeAll(answeredQuestionIds);
                throw new BusinessException(
                    String.format("以下问题未回答: %s", unansweredQuestions)
                );
            }

        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            }
            log.error("验证问卷答案时发生错误", e);
            throw new BusinessException("验证问卷答案失败");
        }
    }
} 
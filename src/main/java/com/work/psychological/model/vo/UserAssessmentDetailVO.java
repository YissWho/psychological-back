package com.work.psychological.model.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAssessmentDetailVO {
    
    private Integer id;
    
    private Integer userId;
    
    private String username;  // 用户名
    
    private Integer assessmentId;
    
    private String assessmentTitle;  // 问卷标题
    
    private List<AnswerDetail> answers;  // 答案详情
    
    private Integer totalScore;
    
    private String resultCategory;
    
    private String resultDescription;  // 结果描述
    
    private String recommendation;  // 建议
    
    private LocalDateTime createdAt;
    
    @Data
    public static class AnswerDetail {
        private Integer questionId;
        private String questionText;  // 问题内容
        private Integer optionId;
        private String optionText;   // 选项内容
        private Integer score;       // 选项得分
    }
} 
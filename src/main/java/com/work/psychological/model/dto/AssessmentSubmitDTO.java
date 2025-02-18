package com.work.psychological.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class AssessmentSubmitDTO {
    
    @NotNull(message = "问卷ID不能为空")
    private Integer assessmentId;
    
    @NotNull(message = "答案不能为空")
    @Size(min = 1, message = "至少需要回答一个问题")
    private List<Answer> answers;
    
    @Data
    public static class Answer {
        @NotNull(message = "问题ID不能为空")
        private Integer questionId;
        
        @NotNull(message = "选项ID不能为空")
        private Integer optionId;
    }
} 
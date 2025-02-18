package com.work.psychological.common.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageRequestDTO {
    
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;  // 默认第一页
    
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;  // 默认每页10条
} 
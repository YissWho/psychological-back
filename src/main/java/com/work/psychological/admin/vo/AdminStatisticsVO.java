package com.work.psychological.admin.vo;

import lombok.Data;
import java.util.List;

/**
 * 管理员统计数据VO
 */
@Data
public class AdminStatisticsVO {
    /**
     * 多个图表数据
     */
    private List<ChartDataVO> charts;
} 
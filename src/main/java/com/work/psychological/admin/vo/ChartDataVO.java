package com.work.psychological.admin.vo;

import lombok.Data;
import java.util.List;

/**
 * 图表数据VO
 */
@Data
public class ChartDataVO {
    /**
     * 图表标题
     */
    private String title;

    /**
     * 图表类型：pie/bar/line
     */
    private String type;

    /**
     * 图表数据
     */
    private List<ChartItemVO> data;

    /**
     * 图表数据项
     */
    @Data
    public static class ChartItemVO {
        /**
         * 标签
         */
        private String label;

        /**
         * 数值
         */
        private Integer value;

        public ChartItemVO(String label, Integer value) {
            this.label = label;
            this.value = value;
        }
    }
} 
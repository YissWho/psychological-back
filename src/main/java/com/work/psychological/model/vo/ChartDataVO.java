package com.work.psychological.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class ChartDataVO {
    private String title;
    private List<ChartItemVO> data;

    @Data
    public static class ChartItemVO {
        private String label;
        private Integer value;

        public ChartItemVO(String label, Integer value) {
            this.label = label;
            this.value = value;
        }
    }
} 
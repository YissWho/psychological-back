package com.work.psychological.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.work.psychological.admin.dto.PsychologicalRecordQueryDTO;
import com.work.psychological.admin.vo.AdminStatisticsVO;
import com.work.psychological.model.entity.PsychologicalRecord;

public interface AdminPsychologicalRecordService {
    
    /**
     * 分页查询心理档案
     * @param queryDTO 查询参数(包含分页信息、姓名、性别等筛选条件)
     * @return 心理档案列表
     */
    IPage<PsychologicalRecord> listPsychologicalRecords(PsychologicalRecordQueryDTO queryDTO);

    /**
     * 获取统计数据
     * @return 统计图表数据
     */
    AdminStatisticsVO getStatistics();
} 
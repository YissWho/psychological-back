package com.work.psychological.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.work.psychological.admin.dto.ResultExplanationCreateDTO;
import com.work.psychological.admin.dto.ResultExplanationUpdateDTO;
import com.work.psychological.admin.dto.ResultExplanationDeleteDTO;
import com.work.psychological.admin.service.AdminResultExplanationService;
import com.work.psychological.common.exception.BusinessException;
import com.work.psychological.mapper.AssessmentMapper;
import com.work.psychological.mapper.ResultExplanationMapper;
import com.work.psychological.model.entity.Assessment;
import com.work.psychological.model.entity.ResultExplanation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminResultExplanationServiceImpl implements AdminResultExplanationService {

    private final ResultExplanationMapper resultExplanationMapper;
    private final AssessmentMapper assessmentMapper;

    @Override
    public List<ResultExplanation> listByAssessmentId(Integer assessmentId) {
        // 检查问卷是否存在
        checkAssessmentExists(assessmentId);
        
        LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResultExplanation::getAssessmentId, assessmentId)
                   .orderByAsc(ResultExplanation::getMinScore);
        
        return resultExplanationMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public ResultExplanation createResultExplanation(ResultExplanationCreateDTO createDTO) {
        // 检查问卷是否存在
        checkAssessmentExists(createDTO.getAssessmentId());
        
        // 检查分数范围是否合理
        if (createDTO.getMinScore() > createDTO.getMaxScore()) {
            throw new BusinessException("最小分数不能大于最大分数");
        }
        
        // 检查分数范围是否重叠
        checkScoreRangeOverlap(createDTO.getAssessmentId(), createDTO.getMinScore(), 
                             createDTO.getMaxScore(), null);
        
        // 检查类别是否已存在
        checkCategoryExists(createDTO.getAssessmentId(), createDTO.getCategory());
        
        ResultExplanation resultExplanation = new ResultExplanation();
        BeanUtils.copyProperties(createDTO, resultExplanation);
        
        resultExplanationMapper.insert(resultExplanation);
        log.info("创建了新的评分规则: 问卷ID={}, 类别={}", createDTO.getAssessmentId(), createDTO.getCategory());
        
        return resultExplanation;
    }

    @Override
    @Transactional
    public ResultExplanation updateResultExplanation(ResultExplanationUpdateDTO updateDTO) {
        // 检查问卷是否存在
        checkAssessmentExists(updateDTO.getAssessmentId());
        
        // 检查原评分规则是否存在
        ResultExplanation existingExplanation = getExplanation(updateDTO.getAssessmentId(), 
                                                             updateDTO.getOriginalCategory());
        if (existingExplanation == null) {
            throw new BusinessException("评分规则不存在");
        }
        
        // 检查分数范围是否合理
        if (updateDTO.getMinScore() > updateDTO.getMaxScore()) {
            throw new BusinessException("最小分数不能大于最大分数");
        }
        
        // 检查分数范围是否重叠（排除自身）
        checkScoreRangeOverlap(updateDTO.getAssessmentId(), updateDTO.getMinScore(), 
                             updateDTO.getMaxScore(), updateDTO.getOriginalCategory());
        
        // 如果修改了类别，检查新类别是否已存在
        if (!updateDTO.getCategory().equals(updateDTO.getOriginalCategory())) {
            checkCategoryExists(updateDTO.getAssessmentId(), updateDTO.getCategory());
        }
        
        // 删除原有记录
        ResultExplanationDeleteDTO deleteDTO = new ResultExplanationDeleteDTO();
        deleteDTO.setAssessmentId(updateDTO.getAssessmentId());
        deleteDTO.setCategory(updateDTO.getOriginalCategory());
        deleteResultExplanation(deleteDTO);
        
        // 创建新记录
        ResultExplanation resultExplanation = new ResultExplanation();
        BeanUtils.copyProperties(updateDTO, resultExplanation);
        
        resultExplanationMapper.insert(resultExplanation);
        log.info("更新了评分规则: 问卷ID={}, 类别={}", updateDTO.getAssessmentId(), updateDTO.getCategory());
        
        return resultExplanation;
    }

    @Override
    @Transactional
    public void deleteResultExplanation(ResultExplanationDeleteDTO deleteDTO) {
        // 检查评分规则是否存在
        ResultExplanation explanation = getExplanation(deleteDTO.getAssessmentId(), deleteDTO.getCategory());
        if (explanation == null) {
            throw new BusinessException("评分规则不存在");
        }
        
        LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResultExplanation::getAssessmentId, deleteDTO.getAssessmentId())
                   .eq(ResultExplanation::getCategory, deleteDTO.getCategory());
        
        resultExplanationMapper.delete(queryWrapper);
        log.info("删除了评分规则: 问卷ID={}, 类别={}", deleteDTO.getAssessmentId(), deleteDTO.getCategory());
    }

    private void checkAssessmentExists(Integer assessmentId) {
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        if (assessment == null) {
            throw new BusinessException("问卷不存在");
        }
    }

    private void checkScoreRangeOverlap(Integer assessmentId, Integer minScore, Integer maxScore, 
                                      String excludeCategory) {
        LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResultExplanation::getAssessmentId, assessmentId)
                   .and(w -> w
                       .and(i -> i.le(ResultExplanation::getMinScore, minScore)
                                .ge(ResultExplanation::getMaxScore, minScore))
                       .or(i -> i.le(ResultExplanation::getMinScore, maxScore)
                                .ge(ResultExplanation::getMaxScore, maxScore))
                       .or(i -> i.ge(ResultExplanation::getMinScore, minScore)
                                .le(ResultExplanation::getMaxScore, maxScore)));
        
        if (excludeCategory != null) {
            queryWrapper.ne(ResultExplanation::getCategory, excludeCategory);
        }
        
        if (resultExplanationMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("分数范围与现有规则重叠");
        }
    }

    private void checkCategoryExists(Integer assessmentId, String category) {
        ResultExplanation existing = getExplanation(assessmentId, category);
        if (existing != null) {
            throw new BusinessException("该类别已存在");
        }
    }

    private ResultExplanation getExplanation(Integer assessmentId, String category) {
        LambdaQueryWrapper<ResultExplanation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResultExplanation::getAssessmentId, assessmentId)
                   .eq(ResultExplanation::getCategory, category);
        return resultExplanationMapper.selectOne(queryWrapper);
    }
} 
package com.stroke.service;

import com.stroke.dto.HematomaExpansionRequest;
import com.stroke.dto.HematomaExpansionResponse;
import com.stroke.model.ExamLookup;
import com.stroke.model.ImagingVolume;
import com.stroke.model.PatientInfo;
import com.stroke.repository.ExamLookupRepository;
import com.stroke.repository.ImagingVolumeRepository;
import com.stroke.repository.PatientInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class HematomaExpansionService {
    private static final double ABSOLUTE_INCREASE_THRESHOLD = 6000.0; // 6 mL = 6000 mm³
    private static final double RELATIVE_INCREASE_THRESHOLD = 0.33; // 33%
    private static final long MAX_HOURS = 48; // 48 hours

    @Autowired
    private PatientInfoRepository patientInfoRepository;

    @Autowired
    private ExamLookupRepository examLookupRepository;

    @Autowired
    private ImagingVolumeRepository imagingVolumeRepository;

    @Transactional(rollbackFor = Exception.class)
    public HematomaExpansionResponse calculateHematomaExpansion(HematomaExpansionRequest request) {
        if (request.getPatientIds() == null || request.getPatientIds().isEmpty()) {
            throw new IllegalArgumentException("患者 ID 列表不能为空");
        }

        HematomaExpansionResponse response = new HematomaExpansionResponse();
        List<HematomaExpansionResponse.HematomaExpansionResult> results = new ArrayList<>();
        int updatedCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (String patientId : request.getPatientIds()) {
            try {
                log.info("开始处理患者 {} 的血肿扩张计算", patientId);

                // 1. 获取表1数据：入院首次影像检查流水号和发病到首次影像检查时间间隔
                Optional<PatientInfo> patientInfoOpt = patientInfoRepository.findById(patientId);
                if (!patientInfoOpt.isPresent()) {
                    String errorMsg = String.format("患者 %s 不存在", patientId);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                PatientInfo patientInfo = patientInfoOpt.get();
                String initialExamCode = patientInfo.getImagingExamNumber();
                BigDecimal onsetToImagingHours = patientInfo.getOnsetToImagingHours();

                if (initialExamCode == null || onsetToImagingHours == null) {
                    String errorMsg = String.format("患者 %s 缺少首次检查流水号或发病到首次检查时间", patientId);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                // 2. 获取首次检查的HM_volume
                Optional<ImagingVolume> initialVolumeOpt = imagingVolumeRepository.findById(initialExamCode);
                if (!initialVolumeOpt.isPresent()) {
                    String errorMsg = String.format("患者 %s 首次检查 %s 的体积数据缺失", patientId, initialExamCode);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                double initialHmVolume = initialVolumeOpt.get().getHmVolume();
                if (initialHmVolume <= 0) {
                    String errorMsg = String.format("患者 %s 首次检查 %s 的体积数据无效", patientId, initialExamCode);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                // 3. 获取检查时间信息 - 修改查询逻辑
                Optional<ExamLookup> examLookupOpt = examLookupRepository.findByInitialExamCode(initialExamCode);
                if (!examLookupOpt.isPresent()) {
                    String errorMsg = String.format("患者 %s 的首次检查 %s 没有检查记录", patientId, initialExamCode);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                ExamLookup examLookup = examLookupOpt.get();
                LocalDateTime initialExamTime = examLookup.getInitialExamTime();
                if (initialExamTime == null) {
                    String errorMsg = String.format("患者 %s 首次检查 %s 的时间缺失", patientId, initialExamCode);
                    log.error(errorMsg);
                    errorMessages.add(errorMsg);
                    continue;
                }

                // 4. 计算发病时间
                LocalDateTime onsetTime = initialExamTime.minusHours(onsetToImagingHours.longValue());

                // 5. 获取48小时内的随访检查
                List<FollowupExam> followupExams = getFollowupExamsWithin48Hours(examLookup, onsetTime);
                if (followupExams.isEmpty()) {
                    log.info("患者 {} 没有48小时内的随访检查", patientId);
                    updatePatientResult(patientId, false, results, updatedCount);
                    continue;
                }

                // 6. 检查每个随访检查的体积变化
                boolean hasExpansion = false;
                for (FollowupExam followup : followupExams) {
                    Optional<ImagingVolume> followupVolumeOpt = imagingVolumeRepository.findById(followup.examCode);
                    if (!followupVolumeOpt.isPresent()) {
                        log.warn("随访检查 {} 的体积数据缺失", followup.examCode);
                        continue;
                    }

                    double followupHmVolume = followupVolumeOpt.get().getHmVolume();
                    if (followupHmVolume <= 0) {
                        log.warn("随访检查 {} 的体积数据无效", followup.examCode);
                        continue;
                    }

                    // 计算绝对和相对增加
                    double absoluteIncrease = followupHmVolume - initialHmVolume;
                    double relativeIncrease = (followupHmVolume - initialHmVolume) / initialHmVolume;

                    log.debug("随访检查 {} 计算结果：绝对增加={}, 相对增加={}",
                            followup.examCode, absoluteIncrease, relativeIncrease);

                    // 判断是否发生血肿扩张
                    if (absoluteIncrease >= ABSOLUTE_INCREASE_THRESHOLD
                            || relativeIncrease >= RELATIVE_INCREASE_THRESHOLD) {
                        hasExpansion = true;
                        log.info("患者 {} 发生血肿扩张：绝对增加={}, 相对增加={}",
                                patientId, absoluteIncrease, relativeIncrease);
                        break;
                    }
                }

                // 7. 更新患者结果
                updatePatientResult(patientId, hasExpansion, results, updatedCount);

            } catch (Exception e) {
                String errorMsg = String.format("处理患者 %s 时发生错误: %s", patientId, e.getMessage());
                log.error(errorMsg, e);
                errorMessages.add(errorMsg);
            }
        }

        response.setUpdatedCount(updatedCount);
        response.setResults(results);
        if (!errorMessages.isEmpty()) {
            response.setErrorMessages(errorMessages);
        }
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updatePatientResult(String patientId, boolean hasExpansion,
            List<HematomaExpansionResponse.HematomaExpansionResult> results, int updatedCount) {
        try {
            // 更新数据库
            Optional<PatientInfo> patientInfoOpt = patientInfoRepository.findById(patientId);
            if (patientInfoOpt.isPresent()) {
                PatientInfo patientInfo = patientInfoOpt.get();
                patientInfo.setHematomaExpansionEvent(hasExpansion);
                patientInfoRepository.save(patientInfo);
                updatedCount++;
            }

            // 添加到结果列表
            HematomaExpansionResponse.HematomaExpansionResult result = new HematomaExpansionResponse.HematomaExpansionResult();
            result.setPatientId(patientId);
            result.setHematomaExpansionEvent(hasExpansion);
            results.add(result);

            log.info("患者 {} 的血肿扩张计算完成，结果: {}", patientId, hasExpansion);
        } catch (Exception e) {
            log.error("更新患者 {} 结果时发生错误: {}", patientId, e.getMessage());
            throw e; // 重新抛出异常以确保事务回滚
        }
    }

    private List<FollowupExam> getFollowupExamsWithin48Hours(ExamLookup examLookup, LocalDateTime onsetTime) {
        List<FollowupExam> followupExams = new ArrayList<>();

        // 检查所有可能的随访时间点
        addFollowupIfWithin48Hours(examLookup.getFollowup1Time(), examLookup.getFollowup1Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup2Time(), examLookup.getFollowup2Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup3Time(), examLookup.getFollowup3Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup4Time(), examLookup.getFollowup4Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup5Time(), examLookup.getFollowup5Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup6Time(), examLookup.getFollowup6Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup7Time(), examLookup.getFollowup7Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup8Time(), examLookup.getFollowup8Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup9Time(), examLookup.getFollowup9Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup10Time(), examLookup.getFollowup10Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup11Time(), examLookup.getFollowup11Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup12Time(), examLookup.getFollowup12Code(), onsetTime,
                followupExams);
        addFollowupIfWithin48Hours(examLookup.getFollowup13Time(), examLookup.getFollowup13Code(), onsetTime,
                followupExams);

        return followupExams;
    }

    private void addFollowupIfWithin48Hours(LocalDateTime followupTime, String followupCode,
            LocalDateTime onsetTime, List<FollowupExam> followupExams) {
        if (followupTime != null && followupCode != null) {
            // 确保随访时间在发病时间之后
            if (followupTime.isBefore(onsetTime)) {
                log.debug("随访时间 {} 在发病时间 {} 之前，跳过", followupTime, onsetTime);
                return;
            }
            long hoursBetween = Duration.between(onsetTime, followupTime).toHours();
            if (hoursBetween > 0 && hoursBetween <= MAX_HOURS) {
                followupExams.add(new FollowupExam(followupCode, followupTime));
                log.debug("添加随访检查 {}，距离发病时间 {} 小时", followupCode, hoursBetween);
            }
        }
    }

    @Data
    @AllArgsConstructor
    private static class FollowupExam {
        private String examCode;
        private LocalDateTime examTime;
    }
}
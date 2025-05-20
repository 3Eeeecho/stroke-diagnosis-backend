package com.stroke.service;

import com.stroke.dto.HematomaExpansionRequest;
import com.stroke.dto.HematomaExpansionResponse;
import com.stroke.model.ExamLookup;
import com.stroke.model.ImagingVolume;
import com.stroke.model.PatientInfo;
import com.stroke.repository.ExamLookupRepository;
import com.stroke.repository.ImagingVolumeRepository;
import com.stroke.repository.PatientInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HematomaExpansionServiceTest {

    @InjectMocks
    private HematomaExpansionService hematomaExpansionService;

    @Mock
    private PatientInfoRepository patientInfoRepository;

    @Mock
    private ExamLookupRepository examLookupRepository;

    @Mock
    private ImagingVolumeRepository imagingVolumeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateHematomaExpansion_WithExpansion() {
        // 准备测试数据
        String patientId = "sub001";
        String initialExamCode = "202501010001";
        LocalDateTime initialExamTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime followupTime = LocalDateTime.of(2025, 1, 1, 20, 0); // 10小时后
        String followupCode = "202501010002";

        // 模拟 PatientInfo
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientId(patientId);
        patientInfo.setImagingExamNumber(initialExamCode);
        patientInfo.setOnsetToImagingHours(new BigDecimal("2.0")); // 发病2小时后首次检查
        when(patientInfoRepository.findById(patientId)).thenReturn(Optional.of(patientInfo));

        // 模拟 ExamLookup
        ExamLookup examLookup = new ExamLookup();
        examLookup.setInitialExamCode(initialExamCode);
        examLookup.setInitialExamTime(initialExamTime);
        examLookup.setFollowup1Time(followupTime);
        examLookup.setFollowup1Code(followupCode);
        when(examLookupRepository.findByInitialExamCode(initialExamCode)).thenReturn(Optional.of(examLookup));

        // 模拟 ImagingVolume - 设置足够大的体积增加以触发扩张
        ImagingVolume initialVolume = new ImagingVolume();
        initialVolume.setExamCode(initialExamCode);
        initialVolume.setHmVolume(10000); // 10 mL
        when(imagingVolumeRepository.findById(initialExamCode)).thenReturn(Optional.of(initialVolume));

        ImagingVolume followupVolume = new ImagingVolume();
        followupVolume.setExamCode(followupCode);
        followupVolume.setHmVolume(20000); // 20 mL，增加10 mL，超过6 mL阈值
        when(imagingVolumeRepository.findById(followupCode)).thenReturn(Optional.of(followupVolume));

        // 执行测试
        HematomaExpansionRequest request = new HematomaExpansionRequest();
        request.setPatientIds(Arrays.asList(patientId));
        HematomaExpansionResponse response = hematomaExpansionService.calculateHematomaExpansion(request);

        // 验证结果
        assertNotNull(response);
        assertEquals(1, response.getResults().size());
        assertTrue(response.getResults().get(0).isHematomaExpansionEvent());
        assertEquals(0, response.getErrorMessages().size());

        // 验证数据库更新
        verify(patientInfoRepository, times(1)).save(argThat(patient -> patient.getPatientId().equals(patientId) &&
                patient.getHematomaExpansionEvent() == true));
    }
}
package com.stroke.service;

import com.stroke.dto.PatientInfoRequest;
import com.stroke.dto.PatientInfoResponse;
import com.stroke.model.PatientInfo;
import com.stroke.repository.PatientInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class PatientInfoService {
    @Autowired
    private PatientInfoRepository patientInfoRepository;

    public PatientInfoResponse createPatient(PatientInfoRequest request) {
        if (patientInfoRepository.existsById(request.getPatientId())) {
            throw new IllegalArgumentException("患者ID已存在");
        }
        PatientInfo patient = new PatientInfo();
        BeanUtils.copyProperties(request, patient);
        patient = patientInfoRepository.save(patient);
        return toResponse(patient);
    }

    public void deletePatient(String patientId) {
        if (!patientInfoRepository.existsById(patientId)) {
            throw new IllegalArgumentException("患者不存在");
        }
        patientInfoRepository.deleteById(patientId);
    }

    public PatientInfoResponse updatePatient(String patientId, PatientInfoRequest request) {
        PatientInfo patient = patientInfoRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("患者不存在"));
        // 只更新非null字段
        if (request.getMrs90Days() != null)
            patient.setMrs90Days(request.getMrs90Days());
        if (request.getImagingExamNumber() != null)
            patient.setImagingExamNumber(request.getImagingExamNumber());
        if (request.getAge() != null)
            patient.setAge(request.getAge());
        if (request.getGender() != null)
            patient.setGender(request.getGender());
        if (request.getPreIchMrs() != null)
            patient.setPreIchMrs(request.getPreIchMrs());
        if (request.getHypertensionHistory() != null)
            patient.setHypertensionHistory(request.getHypertensionHistory());
        if (request.getStrokeHistory() != null)
            patient.setStrokeHistory(request.getStrokeHistory());
        if (request.getDiabetesHistory() != null)
            patient.setDiabetesHistory(request.getDiabetesHistory());
        if (request.getAtrialFibrillationHistory() != null)
            patient.setAtrialFibrillationHistory(request.getAtrialFibrillationHistory());
        if (request.getCoronaryHeartDiseaseHistory() != null)
            patient.setCoronaryHeartDiseaseHistory(request.getCoronaryHeartDiseaseHistory());
        if (request.getSmokingHistory() != null)
            patient.setSmokingHistory(request.getSmokingHistory());
        if (request.getAlcoholHistory() != null)
            patient.setAlcoholHistory(request.getAlcoholHistory());
        if (request.getOnsetToImagingHours() != null)
            patient.setOnsetToImagingHours(request.getOnsetToImagingHours());
        if (request.getBloodPressure() != null)
            patient.setBloodPressure(request.getBloodPressure());
        if (request.getVentricularDrainage() != null)
            patient.setVentricularDrainage(request.getVentricularDrainage());
        if (request.getHemostaticTreatment() != null)
            patient.setHemostaticTreatment(request.getHemostaticTreatment());
        if (request.getIntracranialPressureReduction() != null)
            patient.setIntracranialPressureReduction(request.getIntracranialPressureReduction());
        if (request.getAntihypertensiveTreatment() != null)
            patient.setAntihypertensiveTreatment(request.getAntihypertensiveTreatment());
        if (request.getSedationAnalgesia() != null)
            patient.setSedationAnalgesia(request.getSedationAnalgesia());
        if (request.getAntiemeticGastroprotection() != null)
            patient.setAntiemeticGastroprotection(request.getAntiemeticGastroprotection());
        if (request.getNeurotrophicTreatment() != null)
            patient.setNeurotrophicTreatment(request.getNeurotrophicTreatment());
        patient = patientInfoRepository.save(patient);
        return toResponse(patient);
    }

    public PatientInfoResponse getPatient(String patientId) {
        PatientInfo patient = patientInfoRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("患者不存在"));
        return toResponse(patient);
    }

    public List<PatientInfoResponse> getPatients(int page, int size, Integer age, Integer mrs90Days,
            String imagingExamNumber) {
        Page<PatientInfo> patientPage;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (age != null) {
            patientPage = patientInfoRepository.findByAgeGreaterThanEqual(age, pageRequest);
        } else if (mrs90Days != null) {
            patientPage = patientInfoRepository.findByMrs90Days(mrs90Days, pageRequest);
        } else if (imagingExamNumber != null && !imagingExamNumber.isEmpty()) {
            patientPage = patientInfoRepository.findByImagingExamNumberContaining(imagingExamNumber, pageRequest);
        } else {
            patientPage = patientInfoRepository.findAll(pageRequest);
        }
        return patientPage.getContent().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, Object> getPatientStats() {
        Map<String, Object> stats = new HashMap<>();
        // 获取总患者数
        long totalPatients = patientInfoRepository.count();
        stats.put("totalPatients", totalPatients);

        // 获取高风险患者数（mRS评分大于等于4的患者）
        long highRiskPatients = patientInfoRepository.countByMrs90DaysGreaterThanEqual(4);
        stats.put("highRiskPatients", highRiskPatients);

        return stats;
    }

    public long getPatientsTotal(Integer age, Integer mrs90Days, String imagingExamNumber) {
        if (age != null) {
            return patientInfoRepository.countByAgeGreaterThanEqual(age);
        } else if (mrs90Days != null) {
            return patientInfoRepository.countByMrs90Days(mrs90Days);
        } else if (imagingExamNumber != null && !imagingExamNumber.isEmpty()) {
            return patientInfoRepository.countByImagingExamNumberContaining(imagingExamNumber);
        } else {
            return patientInfoRepository.count();
        }
    }

    private PatientInfoResponse toResponse(PatientInfo patient) {
        PatientInfoResponse response = new PatientInfoResponse();
        BeanUtils.copyProperties(patient, response);
        return response;
    }
}
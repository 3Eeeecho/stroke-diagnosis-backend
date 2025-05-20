package com.stroke.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PatientInfoResponse {
    private String patientId;
    private Integer mrs90Days;
    private String imagingExamNumber;
    private Integer age;
    private String gender;
    private Integer preIchMrs;
    private Boolean hypertensionHistory;
    private Boolean strokeHistory;
    private Boolean diabetesHistory;
    private Boolean atrialFibrillationHistory;
    private Boolean coronaryHeartDiseaseHistory;
    private Boolean smokingHistory;
    private Boolean alcoholHistory;
    private BigDecimal onsetToImagingHours;
    private String bloodPressure;
    private Boolean ventricularDrainage;
    private Boolean hemostaticTreatment;
    private Boolean intracranialPressureReduction;
    private Boolean antihypertensiveTreatment;
    private Boolean sedationAnalgesia;
    private Boolean antiemeticGastroprotection;
    private Boolean neurotrophicTreatment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
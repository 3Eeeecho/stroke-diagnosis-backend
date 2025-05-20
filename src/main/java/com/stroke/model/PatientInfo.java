package com.stroke.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "patient_info")
public class PatientInfo {
    @Id
    @Column(name = "patient_id", length = 10)
    private String patientId;

    @Column(name = "mrs_90days")
    private Integer mrs90Days;

    @Column(name = "imaging_exam_number", length = 20)
    private String imagingExamNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 5)
    private String gender;

    @Column(name = "pre_ich_mrs")
    private Integer preIchMrs;

    @Column(name = "hypertension_history")
    private Boolean hypertensionHistory;

    @Column(name = "stroke_history")
    private Boolean strokeHistory;

    @Column(name = "diabetes_history")
    private Boolean diabetesHistory;

    @Column(name = "atrial_fibrillation_history")
    private Boolean atrialFibrillationHistory;

    @Column(name = "coronary_heart_disease_history")
    private Boolean coronaryHeartDiseaseHistory;

    @Column(name = "smoking_history")
    private Boolean smokingHistory;

    @Column(name = "alcohol_history")
    private Boolean alcoholHistory;

    @Column(name = "onset_to_imaging_hours", precision = 5, scale = 2)
    private BigDecimal onsetToImagingHours;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "ventricular_drainage")
    private Boolean ventricularDrainage;

    @Column(name = "hemostatic_treatment")
    private Boolean hemostaticTreatment;

    @Column(name = "intracranial_pressure_reduction")
    private Boolean intracranialPressureReduction;

    @Column(name = "antihypertensive_treatment")
    private Boolean antihypertensiveTreatment;

    @Column(name = "sedation_analgesia")
    private Boolean sedationAnalgesia;

    @Column(name = "antiemetic_gastroprotection")
    private Boolean antiemeticGastroprotection;

    @Column(name = "neurotrophic_treatment")
    private Boolean neurotrophicTreatment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
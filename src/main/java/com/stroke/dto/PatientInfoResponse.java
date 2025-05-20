package com.stroke.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "患者信息响应体，返回患者详细信息")
public class PatientInfoResponse {
    @Schema(description = "患者唯一标识，最大长度 10 字符", example = "sub001")
    private String patientId;

    @Schema(description = "90 天改良 Rankin 量表（mRS）评分，0-6", example = "2")
    private Integer mrs90Days;

    @Schema(description = "影像检查编号，最大长度 20 字符", example = "IMG001")
    private String imagingExamNumber;

    @Schema(description = "患者年龄", example = "55")
    private Integer age;

    @Schema(description = "性别，最大长度 5 字符", example = "Male")
    private String gender;

    @Schema(description = "出血性脑卒中前 mRS 评分，0-6", example = "1")
    private Integer preIchMrs;

    @Schema(description = "是否患有高血压病史", example = "true")
    private Boolean hypertensionHistory;

    @Schema(description = "是否患有脑卒中病史", example = "false")
    private Boolean strokeHistory;

    @Schema(description = "是否患有糖尿病病史", example = "false")
    private Boolean diabetesHistory;

    @Schema(description = "是否患有心房颤动病史", example = "false")
    private Boolean atrialFibrillationHistory;

    @Schema(description = "是否患有冠心病史", example = "false")
    private Boolean coronaryHeartDiseaseHistory;

    @Schema(description = "是否吸烟", example = "true")
    private Boolean smokingHistory;

    @Schema(description = "是否饮酒", example = "false")
    private Boolean alcoholHistory;

    @Schema(description = "从症状发作到影像检查的时间（小时），精度 5 位，小数点后 2 位", example = "2.50")
    private BigDecimal onsetToImagingHours;

    @Schema(description = "血压读数，格式如 '收缩压/舒张压'，最大长度 20 字符", example = "140/90")
    private String bloodPressure;

    @Schema(description = "是否进行脑室引流", example = "false")
    private Boolean ventricularDrainage;

    @Schema(description = "是否进行止血治疗", example = "true")
    private Boolean hemostaticTreatment;

    @Schema(description = "是否进行降颅内压治疗", example = "false")
    private Boolean intracranialPressureReduction;

    @Schema(description = "是否进行降压治疗", example = "true")
    private Boolean antihypertensiveTreatment;

    @Schema(description = "是否使用镇静或镇痛治疗", example = "false")
    private Boolean sedationAnalgesia;

    @Schema(description = "是否使用止吐或胃保护治疗", example = "true")
    private Boolean antiemeticGastroprotection;

    @Schema(description = "是否进行神经营养治疗", example = "false")
    private Boolean neurotrophicTreatment;

    @Schema(description = "是否发生血肿扩张事件", example = "true")
    private Boolean hematomaExpansionEvent;

    @Schema(description = "记录创建时间，ISO 8601 格式", example = "2025-05-20T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "记录更新时间，ISO 8601 格式", example = "2025-05-20T10:00:00")
    private LocalDateTime updatedAt;
}
package com.stroke.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "血肿扩张事件计算响应体")
public class HematomaExpansionResponse {
    @Schema(description = "更新成功的患者数量", example = "100")
    private int updatedCount;

    @Schema(description = "每个患者的计算结果")
    private List<HematomaExpansionResult> results = new ArrayList<>();

    @Schema(description = "错误消息列表")
    private List<String> errorMessages = new ArrayList<>();

    @Data
    @Schema(description = "单患者的血肿扩张结果")
    public static class HematomaExpansionResult {
        @Schema(description = "患者 ID", example = "sub001")
        private String patientId;

        @Schema(description = "患者年龄", example = "55")
        private Integer age;

        @Schema(description = "患者性别", example = "男")
        private String gender;

        @Schema(description = "首次检查时间", example = "2024-03-20T10:00:00")
        private LocalDateTime firstExamTime;

        @Schema(description = "首次血肿体积(mL)", example = "5.5")
        private Double firstHematomaVolume;

        @Schema(description = "随访检查时间", example = "2024-03-21T10:00:00")
        private LocalDateTime followupExamTime;

        @Schema(description = "随访血肿体积(mL)", example = "8.2")
        private Double followupHematomaVolume;

        @Schema(description = "体积增加量(mL)", example = "2.7")
        private Double volumeIncrease;

        @Schema(description = "相对增加率", example = "0.49")
        private Double relativeIncrease;

        @Schema(description = "是否发生血肿扩张事件", example = "true")
        private boolean hematomaExpansionEvent;
    }
}
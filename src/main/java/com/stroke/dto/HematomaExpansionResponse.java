package com.stroke.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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

        @Schema(description = "是否发生血肿扩张事件", example = "true")
        private boolean hematomaExpansionEvent;
    }
}
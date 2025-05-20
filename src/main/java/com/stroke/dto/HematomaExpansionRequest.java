package com.stroke.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "血肿扩张事件计算请求体")
public class HematomaExpansionRequest {
    @Schema(description = "患者 ID 列表", example = "[\"sub001\", \"sub002\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> patientIds;
}
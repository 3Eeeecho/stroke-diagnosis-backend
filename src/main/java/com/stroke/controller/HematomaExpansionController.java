package com.stroke.controller;

import com.stroke.dto.HematomaExpansionRequest;
import com.stroke.dto.HematomaExpansionResponse;
import com.stroke.service.HematomaExpansionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "血肿扩张管理", description = "计算和管理血肿扩张事件的 API")
@RestController
@RequestMapping("/hematoma-expansion")
public class HematomaExpansionController {
    @Autowired
    private HematomaExpansionService hematomaExpansionService;

    @Operation(summary = "计算血肿扩张事件", description = "为指定患者列表计算发病后 48 小时内的血肿扩张事件，更新数据库")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "计算成功，返回更新结果"),
            @ApiResponse(responseCode = "400", description = "患者 ID 列表无效或数据缺失"),
            @ApiResponse(responseCode = "403", description = "未授权，JWT 无效或缺失"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateHematomaExpansion(@RequestBody HematomaExpansionRequest request) {
        try {
            HematomaExpansionResponse response = hematomaExpansionService.calculateHematomaExpansion(request);
            return ResponseEntity.ok(new ApiResult(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "计算失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "计算单个患者血肿扩张事件", description = "为单个患者计算发病后 48 小时内的血肿扩张事件")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "计算成功，返回更新结果"),
            @ApiResponse(responseCode = "400", description = "患者 ID 无效或数据缺失"),
            @ApiResponse(responseCode = "403", description = "未授权，JWT 无效或缺失"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/calculate/{patientId}")
    public ResponseEntity<?> calculateSinglePatient(@PathVariable String patientId) {
        try {
            HematomaExpansionRequest request = new HematomaExpansionRequest();
            request.setPatientIds(List.of(patientId));
            HematomaExpansionResponse response = hematomaExpansionService.calculateHematomaExpansion(request);
            return ResponseEntity.ok(new ApiResult(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "计算失败: " + e.getMessage()));
        }
    }

    private static class ApiResult {
        private int code;
        private Object data;

        public ApiResult(int code, Object data) {
            this.code = code;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public Object getData() {
            return data;
        }
    }
}
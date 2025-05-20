package com.stroke.controller;

import com.stroke.dto.PatientInfoRequest;
import com.stroke.dto.PatientInfoResponse;
import com.stroke.service.PatientInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/patients")
@Tag(name = "患者信息管理", description = "患者信息的增删改查接口")
public class PatientInfoController {
    @Autowired
    private PatientInfoService patientInfoService;

    @Operation(summary = "创建患者信息", description = "创建新的患者信息记录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(schema = @Schema(implementation = PatientInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public ResponseEntity<?> createPatient(
            @Parameter(description = "患者信息请求参数", required = true) @RequestBody PatientInfoRequest request) {
        try {
            PatientInfoResponse response = patientInfoService.createPatient(request);
            return ResponseEntity.ok(new ApiResult(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "创建失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "删除患者信息", description = "根据ID删除患者信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(
            @Parameter(description = "患者ID", required = true) @PathVariable String id) {
        try {
            patientInfoService.deletePatient(id);
            return ResponseEntity.ok(new ApiResult(200, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "删除失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "获取患者统计数据", description = "获取患者总数和高风险患者数量")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats")
    public ResponseEntity<?> getPatientStats() {
        try {
            Map<String, Object> stats = patientInfoService.getPatientStats();
            return ResponseEntity.ok(new ApiResult(200, stats));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "获取统计数据失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "更新患者信息", description = "根据ID更新患者信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(schema = @Schema(implementation = PatientInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(
            @Parameter(description = "患者ID", required = true) @PathVariable String id,
            @Parameter(description = "患者信息请求参数", required = true) @RequestBody PatientInfoRequest request) {
        try {
            PatientInfoResponse response = patientInfoService.updatePatient(id, request);
            return ResponseEntity.ok(new ApiResult(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "更新失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "获取单个患者信息", description = "根据ID获取患者详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(schema = @Schema(implementation = PatientInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatient(
            @Parameter(description = "患者ID", required = true) @PathVariable String id) {
        try {
            PatientInfoResponse response = patientInfoService.getPatient(id);
            return ResponseEntity.ok(new ApiResult(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "查询失败: " + e.getMessage()));
        }
    }

    @Operation(summary = "获取患者列表", description = "分页获取患者信息列表，支持多种筛选条件")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(schema = @Schema(implementation = PatientInfoResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping
    public ResponseEntity<?> getPatients(
            @Parameter(description = "页码，从0开始", required = false) @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "年龄筛选", required = false) @RequestParam(required = false) Integer age,
            @Parameter(description = "90天mRS评分筛选", required = false) @RequestParam(required = false) Integer mrs90Days,
            @Parameter(description = "影像学检查编号筛选", required = false) @RequestParam(required = false) String imagingExamNumber) {
        try {
            List<PatientInfoResponse> records = patientInfoService.getPatients(page, size, age, mrs90Days,
                    imagingExamNumber);
            long total = patientInfoService.getPatientsTotal(age, mrs90Days, imagingExamNumber);
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            return ResponseEntity.ok(new ApiResult(200, result));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResult(500, "查询失败: " + e.getMessage()));
        }
    }

    @Schema(description = "API响应数据")
    private static class ApiResult {
        @Schema(description = "响应状态码")
        private int code;

        @Schema(description = "响应数据")
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
package com.stroke.controller;

import com.stroke.dto.PatientInfoRequest;
import com.stroke.dto.PatientInfoResponse;
import com.stroke.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientInfoController {
    @Autowired
    private PatientInfoService patientInfoService;

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody PatientInfoRequest request) {
        try {
            PatientInfoResponse response = patientInfoService.createPatient(request);
            return ResponseEntity.ok(new ApiResponse(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "创建失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable String patientId) {
        try {
            patientInfoService.deletePatient(patientId);
            return ResponseEntity.ok(new ApiResponse(200, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "删除失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<?> updatePatient(@PathVariable String patientId, @RequestBody PatientInfoRequest request) {
        try {
            PatientInfoResponse response = patientInfoService.updatePatient(patientId, request);
            return ResponseEntity.ok(new ApiResponse(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "更新失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> getPatient(@PathVariable String patientId) {
        try {
            PatientInfoResponse response = patientInfoService.getPatient(patientId);
            return ResponseEntity.ok(new ApiResponse(200, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "查询失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Integer mrs90Days,
            @RequestParam(required = false) String imagingExamNumber) {
        try {
            List<PatientInfoResponse> response = patientInfoService.getPatients(page, size, age, mrs90Days, imagingExamNumber);
            return ResponseEntity.ok(new ApiResponse(200, response));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(500, "查询失败: " + e.getMessage()));
        }
    }

    private static class ApiResponse {
        private int code;
        private Object data;

        public ApiResponse(int code, Object data) {
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
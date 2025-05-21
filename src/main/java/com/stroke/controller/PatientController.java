package com.stroke.controller;

import com.stroke.model.PatientInfo;
import com.stroke.service.PatientInfoService;
import com.stroke.service.HematomaExpansionService;
import com.stroke.dto.ApiResponse;
import com.stroke.dto.HematomaExpansionRequest;
import com.stroke.dto.HematomaExpansionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientInfoService patientInfoService;

    @Autowired
    private HematomaExpansionService hematomaExpansionService;

    @GetMapping("/hematoma-expansion")
    public ResponseEntity<?> getHematomaExpansionPatients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // 1. 获取血肿扩张患者基本信息
            Page<PatientInfo> patients = patientInfoService.findHematomaExpansionPatients(page - 1, size);

            // 2. 获取每个患者的详细血肿扩张信息
            List<HematomaExpansionResponse.HematomaExpansionResult> detailedResults = new ArrayList<>();

            for (PatientInfo patient : patients.getContent()) {
                // 为每个患者创建请求
                HematomaExpansionRequest request = new HematomaExpansionRequest();
                request.setPatientIds(List.of(patient.getPatientId()));

                // 获取详细的血肿扩张信息
                HematomaExpansionResponse response = hematomaExpansionService.calculateHematomaExpansion(request);
                if (!response.getResults().isEmpty()) {
                    detailedResults.add(response.getResults().get(0));
                }
            }

            // 3. 构建分页响应
            Page<HematomaExpansionResponse.HematomaExpansionResult> detailedPage = new PageImpl<>(
                    detailedResults,
                    patients.getPageable(),
                    patients.getTotalElements());

            return ResponseEntity.ok(new ApiResponse<>(200, "获取血肿扩张患者列表成功", detailedPage));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "获取血肿扩张患者列表失败: " + e.getMessage(), null));
        }
    }
}
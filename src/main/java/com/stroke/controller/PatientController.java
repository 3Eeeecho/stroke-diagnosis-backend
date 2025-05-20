package com.stroke.controller;

import com.stroke.model.PatientInfo;
import com.stroke.service.PatientInfoService;
import com.stroke.service.HematomaExpansionService;
import com.stroke.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Page<PatientInfo> patients = patientInfoService.findHematomaExpansionPatients(page - 1, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "获取血肿扩张患者列表成功", patients));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "获取血肿扩张患者列表失败: " + e.getMessage(), null));
        }
    }
}
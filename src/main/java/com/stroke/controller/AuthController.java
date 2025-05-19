package com.stroke.controller;

import com.stroke.config.JwtUtil;
import com.stroke.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Received login request for username: " + loginRequest.getUsername());
        boolean isValid = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValid) {
            try {
                String token = jwtUtil.generateToken(loginRequest.getUsername());
                System.out.println("Login successful, token generated: " + token);
                return ResponseEntity.ok(new ApiResponse(200, new LoginResponse(token)));
            } catch (Exception e) {
                System.out.println("JWT generation failed: " + e.getMessage());
                return ResponseEntity.ok(new ApiResponse(500, "令牌生成失败: " + e.getMessage()));
            }
        }
        System.out.println("Login failed for username: " + loginRequest.getUsername());
        return ResponseEntity.ok(new ApiResponse(401, "无效的用户名或密码"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest) {
        System.out.println("Received register request for username: " + loginRequest.getUsername());
        try {
            userService.registerUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new ApiResponse(200, null));
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return ResponseEntity.ok(new ApiResponse(400, e.getMessage()));
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            return ResponseEntity.ok(new ApiResponse(500, "注册失败: " + e.getMessage()));
        }
    }

    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
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
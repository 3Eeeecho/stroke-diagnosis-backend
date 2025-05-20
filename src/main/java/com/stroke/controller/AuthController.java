package com.stroke.controller;

import com.stroke.config.JwtUtil;
import com.stroke.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户认证相关的API接口")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "用户登录", description = "验证用户凭据并返回JWT令牌")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "无效的用户名或密码"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "登录请求参数", required = true) @RequestBody LoginRequest loginRequest) {
        System.out.println("Received login request for username: " + loginRequest.getUsername());
        boolean isValid = userService.validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValid) {
            try {
                String token = jwtUtil.generateToken(loginRequest.getUsername());
                System.out.println("Login successful, token generated: " + token);
                return ResponseEntity.ok(new ApiResult(200, new LoginResponse(token)));
            } catch (Exception e) {
                System.out.println("JWT generation failed: " + e.getMessage());
                return ResponseEntity.ok(new ApiResult(500, "令牌生成失败: " + e.getMessage()));
            }
        }
        System.out.println("Login failed for username: " + loginRequest.getUsername());
        return ResponseEntity.ok(new ApiResult(401, "无效的用户名或密码"));
    }

    @Operation(summary = "用户注册", description = "注册新用户账号")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Parameter(description = "注册请求参数", required = true) @RequestBody LoginRequest loginRequest) {
        System.out.println("Received register request for username: " + loginRequest.getUsername());
        try {
            userService.registerUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new ApiResult(200, null));
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return ResponseEntity.ok(new ApiResult(400, e.getMessage()));
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            return ResponseEntity.ok(new ApiResult(500, "注册失败: " + e.getMessage()));
        }
    }

    @Schema(description = "登录请求参数")
    private static class LoginRequest {
        @Schema(description = "用户名", required = true)
        private String username;

        @Schema(description = "密码", required = true)
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

    @Schema(description = "登录响应数据")
    private static class LoginResponse {
        @Schema(description = "JWT令牌")
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
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
package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.request.LoginRequest;
import com.backend.librarycrm.dto.request.RegisterRequest;
import com.backend.librarycrm.dto.response.AuthResponse;
import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(BaseResponse.success(response, "Đăng ký thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(BaseResponse.success(response, "Đăng nhập thành công"));
    }
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(BaseResponse.success(null, "Đăng xuất thành công!"));
    }
}
package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.request.UpdateProfileRequest;
import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.model.User;
import com.backend.librarycrm.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. Xem hồ sơ cá nhân
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(user), "Lấy hồ sơ thành công"));
    }

    // 2. Cập nhật thông tin cá nhân
    @PutMapping("/me")
    public ResponseEntity<BaseResponse<Map<String, Object>>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.updateProfile(username, request);
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(user), "Cập nhật hồ sơ thành công"));
    }

    // 3. Đóng tiền nợ phạt
    @PostMapping("/me/pay-fine")
    public ResponseEntity<BaseResponse<Map<String, Object>>> payFine(@RequestParam Double amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.payFine(username, amount);
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(user), "Đóng tiền phạt thành công. Dư nợ hiện tại: " + user.getFineBalance()));
    }


    private Map<String, Object> mapToResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("phone", user.getPhone());
        response.put("role", user.getRole());
        response.put("fineBalance", user.getFineBalance());
        response.put("isActive", user.isActive());
        return response;
    }
}
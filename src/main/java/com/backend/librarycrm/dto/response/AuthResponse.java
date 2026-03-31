package com.backend.librarycrm.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token; // Chuỗi JWT
    private String type = "Bearer";
    private Integer userId;
    private String username;
    private String role;
}
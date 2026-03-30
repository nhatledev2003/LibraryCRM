package com.backend.librarycrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Quyền (Role) không được để trống")
    @Pattern(regexp = "^(ADMIN|LIBRARIAN|READER)$", message = "Role chỉ được là ADMIN, LIBRARIAN hoặc READER")
    @Column(nullable = false)
    private String role;

    @NotBlank(message = "Họ và tên không được để trống")
    @Column(name = "full_name")
    private String fullName;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không hợp lệ định dạng Việt Nam")
    private String phone;

    @Email(message = "Email không đúng định dạng")
    @Column(unique = true)
    private String email;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Min(value = 0, message = "Dư nợ phạt không được là số âm")
    @Column(name = "fine_balance")
    private Double fineBalance = 0.0;
}
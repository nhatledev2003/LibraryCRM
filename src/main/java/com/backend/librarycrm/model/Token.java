package com.backend.librarycrm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 500) // Token thường khá dài
    private String token;

    @Column(nullable = false)
    private String tokenType; // Thường là "BEARER"

    private boolean revoked; // Đã bị thu hồi (khi đăng xuất hoặc đăng nhập máy khác)
    private boolean expired; // Đã hết hạn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
package com.backend.librarycrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "book_instances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookInstance {

    @Id
    @NotBlank(message = "Mã vạch (Barcode) không được để trống")
    @Column(unique = true, nullable = false, length = 50)
    private String barcode; // ID chính của bảng này là chuỗi Barcode (VD: DNT-001)

    // Khai báo khóa ngoại nối tới bảng Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Cuốn sách vật lý phải thuộc về một Đầu sách (Book)")
    private Book book;

    @NotBlank(message = "Trạng thái cuốn sách không được để trống")
    @Pattern(regexp = "^(AVAILABLE|BORROWED|DAMAGED|LOST)$", message = "Trạng thái không hợp lệ")
    @Column(nullable = false)
    private String status;

    @Column(name = "condition_note")
    private String conditionNote; // Ghi chú: "Bị rách trang 15", "Còn mới"...
}
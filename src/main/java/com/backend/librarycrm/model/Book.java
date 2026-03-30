package com.backend.librarycrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã ISBN không được để trống")
    @Column(unique = true, nullable = false)
    private String isbn;

    @NotBlank(message = "Tên sách không được để trống")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Tên tác giả không được để trống")
    private String author;

    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Min(value = 0, message = "Tổng số lượng sách không được nhỏ hơn 0")
    @Column(name = "total_quantity")
    private Integer totalQuantity = 0;

    @Min(value = 0, message = "Số lượng sách sẵn có không được nhỏ hơn 0")
    @Column(name = "available_quantity")
    private Integer availableQuantity = 0;
}

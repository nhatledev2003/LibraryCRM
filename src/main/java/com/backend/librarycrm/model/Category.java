package com.backend.librarycrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@ToString(exclude = {"books"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // Quan hệ 1-N: 1 Thể loại có nhiều Đầu sách
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;
}

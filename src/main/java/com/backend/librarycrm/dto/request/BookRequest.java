package com.backend.librarycrm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "Mã ISBN không được để trống")
    private String isbn;

    @NotBlank(message = "Tên sách không được để trống")
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    private String author;

    private String publisher;

    @NotNull(message = "ID Thể loại không được để trống")
    private Integer categoryId;

    private String coverImageUrl;
}
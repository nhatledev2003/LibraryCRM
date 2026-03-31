package com.backend.librarycrm.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Integer id;
    private String isbn;
    private String title;
    private String author;
    private String categoryName; // Chỉ trả về tên thể loại cho FE hiển thị, không cần nguyên object Category
    private Integer totalQuantity;
    private Integer availableQuantity;
}
package com.backend.librarycrm.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class BorrowRequest {
    @NotNull(message = "ID Độc giả không được để trống")
    private Integer readerId;
    @NotNull(message = "ID của Thủ thư không được để trống")
    private Integer librarianId;
    @NotEmpty(message = "Danh sách mã vạch không được để trống")
    private List<String> barcodes;
}
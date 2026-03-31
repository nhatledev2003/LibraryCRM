package com.backend.librarycrm.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreatePoRequest {
    @NotNull(message = "Nhà cung cấp không được để trống")
    private Integer supplierId;

    @NotEmpty(message = "Danh sách sách nhập không được để trống")
    private List<PoItemRequest> items;

    @Data
    public static class PoItemRequest {
        @NotNull
        private Integer bookId;
        @NotNull
        private Integer quantity;
        @NotNull
        private Double unitPrice;
    }
}
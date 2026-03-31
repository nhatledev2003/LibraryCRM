package com.backend.librarycrm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ReturnRequest {
    @NotNull(message = "ID Phiếu mượn không được để trống")
    private Integer recordId;

    private List<ReturnItemRequest> items;

    @Data
    public static class ReturnItemRequest {
        private String barcode;
        private Double damageFine; // Tiền phạt hỏng hóc cho cuốn này (nếu có)
    }
}
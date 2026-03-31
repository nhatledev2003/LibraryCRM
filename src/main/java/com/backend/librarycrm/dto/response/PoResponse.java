package com.backend.librarycrm.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PoResponse {
    private Integer orderId;
    private String supplierName;
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
}

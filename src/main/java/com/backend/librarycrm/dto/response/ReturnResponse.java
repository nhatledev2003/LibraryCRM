package com.backend.librarycrm.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnResponse {
    private Integer recordId;
    private Double lateFee;        // Phạt quá hạn
    private Double totalDamageFee; // Tổng phạt hỏng hóc
    private Double totalFine;      // Tổng cộng (late + damage)
    private String message;
}
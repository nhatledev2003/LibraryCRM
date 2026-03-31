package com.backend.librarycrm.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BorrowResponse {
    private Integer recordId;
    private String readerName;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private String status;
    private List<String> borrowedBookTitles;
}
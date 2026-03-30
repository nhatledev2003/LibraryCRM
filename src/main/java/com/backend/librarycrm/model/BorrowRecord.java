package com.backend.librarycrm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrow_records")
@Data
@ToString(exclude = {"borrowDetails"})
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ N-1: Nhiều phiếu mượn thuộc về 1 Độc giả
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Phiếu mượn phải gắn với một Độc giả")
    private User user;

    // Quan hệ N-1: Nhiều phiếu mượn được tạo bởi 1 Thủ thư
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @NotNull(message = "Phải ghi nhận người tạo phiếu")
    private User createdBy;

    @Column(name = "borrow_date", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate; // Sẽ được update khi trả sách

    @Column(nullable = false)
    private String status; // BORROWING, RETURNED, OVERDUE

    @Column(name = "total_fine")
    private Double totalFine = 0.0;

    // Quan hệ 1-N: 1 Phiếu mượn có nhiều Chi tiết mượn (nhiều cuốn sách)
    // cascade = CascadeType.ALL giúp khi save BorrowRecord, nó tự save luôn các BorrowDetail bên trong
    @OneToMany(mappedBy = "borrowRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BorrowDetail> borrowDetails;


}
package com.backend.librarycrm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "borrow_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nối ngược lại với Phiếu mượn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private BorrowRecord borrowRecord;

    // Nối với bản sao vật lý của sách (dùng Barcode)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barcode", referencedColumnName = "barcode", nullable = false)
    private BookInstance bookInstance;

    @Column(name = "return_status")
    private String returnStatus; // NORMAL, LATE, DAMAGED

    @Column(name = "item_fine")
    private Double itemFine = 0.0; // Tiền phạt riêng cho cuốn sách này nếu làm hỏng
}
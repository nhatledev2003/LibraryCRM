package com.backend.librarycrm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "po_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    // Nhập đầu sách nào?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;
}
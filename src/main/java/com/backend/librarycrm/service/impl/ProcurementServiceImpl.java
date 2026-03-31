package com.backend.librarycrm.service.impl;

import com.backend.librarycrm.model.*;
import com.backend.librarycrm.repository.BookInstanceRepository;
import com.backend.librarycrm.repository.BookRepository;
import com.backend.librarycrm.repository.PurchaseOrderRepository;
import com.backend.librarycrm.repository.SupplierRepository;
import com.backend.librarycrm.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcurementServiceImpl implements ProcurementService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final BookRepository bookRepository;
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public PurchaseOrder receiveOrder(Integer orderId) {
        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Đơn nhập hàng"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("Đơn hàng này đã được xử lý.");
        }


        for (PoDetail detail : order.getPoDetails()) {
            Book book = detail.getBook();
            int quantityToAdd = detail.getQuantity();


            for (int i = 0; i < quantityToAdd; i++) {

                String generatedBarcode = book.getIsbn() + "-" + UUID.randomUUID().toString().substring(0, 8);

                BookInstance instance = BookInstance.builder()
                        .barcode(generatedBarcode)
                        .book(book)
                        .status("AVAILABLE")
                        .conditionNote("Sách mới nhập")
                        .build();

                bookInstanceRepository.save(instance);
            }


            book.setTotalQuantity(book.getTotalQuantity() + quantityToAdd);
            book.setAvailableQuantity(book.getAvailableQuantity() + quantityToAdd);
            bookRepository.save(book);
        }


        order.setStatus("COMPLETED");
        return purchaseOrderRepository.save(order);
    }

    @Override
    @Transactional
    public PurchaseOrder createOrder(Integer supplierId, List<Integer> bookIds, List<Integer> quantities, List<Double> unitPrices) {


        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Nhà cung cấp với ID: " + supplierId));


        PurchaseOrder order = PurchaseOrder.builder()
                .supplier(supplier)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .build();

        List<PoDetail> details = new ArrayList<>();
        double totalAmount = 0.0;


        for (int i = 0; i < bookIds.size(); i++) {
            int finalI = i;
            Book book = bookRepository.findById(bookIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + bookIds.get(finalI)));

            int qty = quantities.get(i);
            double price = unitPrices.get(i);

            PoDetail detail = PoDetail.builder()
                    .purchaseOrder(order)
                    .book(book)
                    .quantity(qty)
                    .unitPrice(price)
                    .build();

            details.add(detail);
            totalAmount += (qty * price);
        }


        order.setPoDetails(details);
        order.setTotalAmount(totalAmount);

        return purchaseOrderRepository.save(order);
    }
}
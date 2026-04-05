package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.request.CreatePoRequest;
import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.dto.response.PoResponse;
import com.backend.librarycrm.model.PurchaseOrder;
import com.backend.librarycrm.service.ProcurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/procurement")
@RequiredArgsConstructor
public class ProcurementController {

    private final ProcurementService procurementService;

    // 1. Tạo Đơn nhập hàng (Tạo PO)
    @PostMapping("/orders")
    public ResponseEntity<BaseResponse<PoResponse>> createPurchaseOrder(@Valid @RequestBody CreatePoRequest request) {


        List<Integer> bookIds = request.getItems().stream()
                .map(CreatePoRequest.PoItemRequest::getBookId)
                .collect(Collectors.toList());

        List<Integer> quantities = request.getItems().stream()
                .map(CreatePoRequest.PoItemRequest::getQuantity)
                .collect(Collectors.toList());

        List<Double> unitPrices = request.getItems().stream()
                .map(CreatePoRequest.PoItemRequest::getUnitPrice)
                .collect(Collectors.toList());


        PurchaseOrder order = procurementService.createOrder(
                request.getSupplierId(),
                bookIds,
                quantities,
                unitPrices
        );

        return ResponseEntity.ok(BaseResponse.success(mapToResponse(order), "Tạo đơn nhập hàng thành công"));
    }

    // 2. Xác nhận nhận hàng (Cộng tồn kho & Sinh mã vạch)
    @PostMapping("/orders/{id}/receive")
    public ResponseEntity<BaseResponse<PoResponse>> receivePurchaseOrder(@PathVariable Integer id) {
        // Hàm receiveOrder này chúng ta đã code logic sinh mã vạch cực xịn ở các buổi trước
        PurchaseOrder order = procurementService.receiveOrder(id);

        return ResponseEntity.ok(BaseResponse.success(mapToResponse(order), "Nhận hàng và sinh mã vạch thành công"));
    }


    private PoResponse mapToResponse(PurchaseOrder order) {
        return PoResponse.builder()
                .orderId(order.getId())
                .supplierName(order.getSupplier().getName())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}

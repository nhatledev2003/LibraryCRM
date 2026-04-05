package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.request.BorrowRequest;
import com.backend.librarycrm.dto.request.ReturnRequest;
import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.model.BorrowRecord;
import com.backend.librarycrm.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    // 1. Tạo phiếu mượn sách
    @PostMapping
    public ResponseEntity<BaseResponse<Map<String, Object>>> createBorrow(@Valid @RequestBody BorrowRequest request) {

        // Truyền đúng 3 tham số mà hàm service của bạn yêu cầu
        BorrowRecord record = borrowService.borrowBooks(
                request.getReaderId(),
                request.getLibrarianId(),
                request.getBarcodes()
        );

        return ResponseEntity.ok(BaseResponse.success(mapToResponse(record), "Tạo phiếu mượn thành công"));
    }

    // 2. Trả sách
    @PostMapping("/return")
    public ResponseEntity<BaseResponse<Map<String, Object>>> returnBorrow(@Valid @RequestBody ReturnRequest request) {

        // 1. Dùng Stream để tách mảng Object thành 2 mảng rời (barcodes và damageFines)
        List<String> returnedBarcodes = request.getItems().stream()
                .map(ReturnRequest.ReturnItemRequest::getBarcode)
                .collect(Collectors.toList());

        List<Double> damageFines = request.getItems().stream()
                .map(ReturnRequest.ReturnItemRequest::getDamageFine)
                .collect(Collectors.toList());

        // 2. Bơm vào Service của bạn
        BorrowRecord record = borrowService.returnBooks(
                request.getRecordId(),
                returnedBarcodes,
                damageFines
        );

        return ResponseEntity.ok(BaseResponse.success(mapToResponse(record), "Trả sách thành công"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getBorrowRecord(@PathVariable Integer id) {
        BorrowRecord record = borrowService.getBorrowRecordById(id);
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(record), "Lấy chi tiết phiếu mượn thành công"));
    }

    // 4. Xem lịch sử mượn sách của 1 độc giả
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getBorrowHistory(@PathVariable Integer userId) {
        List<BorrowRecord> records = borrowService.getBorrowHistoryByUser(userId);

        List<Map<String, Object>> responseList = records.stream()
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(BaseResponse.success(responseList, "Lấy lịch sử mượn sách thành công"));
    }
    private Map<String, Object> mapToResponse(BorrowRecord record) {
        Map<String, Object> response = new HashMap<>();
        response.put("recordId", record.getId());
        response.put("readerName", record.getUser().getFullName());
        response.put("librarianName", record.getCreatedBy() != null ? record.getCreatedBy().getFullName() : "N/A"); // Thêm tên thủ thư
        response.put("borrowDate", record.getBorrowDate());
        response.put("dueDate", record.getDueDate());
        response.put("returnDate", record.getReturnDate());
        response.put("status", record.getStatus());
        response.put("totalFine", record.getTotalFine());

        // Bổ sung chi tiết các cuốn sách đã mượn
        if (record.getBorrowDetails() != null) {
            response.put("totalItems", record.getBorrowDetails().size());

            List<Map<String, Object>> items = record.getBorrowDetails().stream().map(detail -> {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("barcode", detail.getBookInstance().getBarcode());
                itemMap.put("bookTitle", detail.getBookInstance().getBook().getTitle()); // Hiển thị thêm tên sách cho đẹp
                itemMap.put("returnStatus", detail.getReturnStatus());
                itemMap.put("itemFine", detail.getItemFine());
                return itemMap;
            }).collect(java.util.stream.Collectors.toList());

            response.put("items", items);
        } else {
            response.put("totalItems", 0);
            response.put("items", new java.util.ArrayList<>());
        }

        return response;
    }
}
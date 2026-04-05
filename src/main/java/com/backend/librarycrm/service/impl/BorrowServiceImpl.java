package com.backend.librarycrm.service.impl;

import com.backend.librarycrm.model.*;
import com.backend.librarycrm.repository.BookInstanceRepository;
import com.backend.librarycrm.repository.BookRepository;
import com.backend.librarycrm.repository.BorrowRecordRepository;
import com.backend.librarycrm.repository.UserRepository;
import com.backend.librarycrm.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public BorrowRecord borrowBooks(Integer readerId, Integer librarianId,List<String> barcodes) {


        User reader = userRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Độc giả"));

        if (!reader.isActive() || reader.getFineBalance() > 0) {
            throw new RuntimeException("Độc giả đang bị khóa thẻ hoặc đang nợ tiền phạt. Không thể mượn sách.");
        }


        User librarian = userRepository.findById(librarianId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin Thủ thư"));


        BorrowRecord record = BorrowRecord.builder()
                .user(reader)
                .createdBy(librarian)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(14))
                .status("BORROWING")
                .totalFine(0.0)
                .borrowDetails(new ArrayList<>())
                .build();


        for (String barcode : barcodes) {

            BookInstance instance = bookInstanceRepository.findById(barcode)
                    .orElseThrow(() -> new RuntimeException("Mã vạch không hợp lệ: " + barcode));


            if (!"AVAILABLE".equals(instance.getStatus())) {
                throw new RuntimeException("Cuốn sách có mã " + barcode + " hiện không sẵn sàng để mượn.");
            }


            instance.setStatus("BORROWED");
            bookInstanceRepository.save(instance);


            Book book = instance.getBook();
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            bookRepository.save(book);


            BorrowDetail detail = BorrowDetail.builder()
                    .borrowRecord(record)
                    .bookInstance(instance)
                    .returnStatus("NORMAL")
                    .itemFine(0.0)
                    .build();

            record.getBorrowDetails().add(detail);
        }
        return borrowRecordRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecord returnBooks(Integer recordId, List<String> returnedBarcodes, List<Double> damageFines) {

        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Phiếu mượn"));

        if (!"BORROWING".equals(record.getStatus())) {
            throw new RuntimeException("Phiếu mượn này đã được xử lý xong từ trước.");
        }

        double totalFineCalc = 0.0;
        LocalDateTime now = LocalDateTime.now();
        record.setReturnDate(now);


        long overdueDays = java.time.Duration.between(record.getDueDate(), now).toDays();
        if (overdueDays > 0) {
            totalFineCalc += overdueDays * 5000.0;
        }


        for (int i = 0; i < returnedBarcodes.size(); i++) {
            String barcode = returnedBarcodes.get(i);
            Double damageFine = damageFines.get(i);


            BookInstance instance = bookInstanceRepository.findById(barcode)
                    .orElseThrow(() -> new RuntimeException("Mã vạch không hợp lệ"));

            if (damageFine > 0) {
                instance.setStatus("DAMAGED");
                totalFineCalc += damageFine;
            } else {
                instance.setStatus("AVAILABLE");
                Book book = instance.getBook();
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                bookRepository.save(book);
            }
            bookInstanceRepository.save(instance);


            BorrowDetail detail = record.getBorrowDetails().stream()
                    .filter(d -> d.getBookInstance().getBarcode().equals(barcode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cuốn sách này không nằm trong phiếu mượn"));

            detail.setReturnStatus(damageFine > 0 ? "DAMAGED" : (overdueDays > 0 ? "LATE" : "NORMAL"));
            detail.setItemFine(damageFine);
        }


        record.setStatus(overdueDays > 0 ? "OVERDUE" : "RETURNED");
        record.setTotalFine(totalFineCalc);

        if (totalFineCalc > 0) {
            User reader = record.getUser();
            reader.setFineBalance(reader.getFineBalance() + totalFineCalc);
            userRepository.save(reader);
        }

        return borrowRecordRepository.save(record);
    }

    @Override
    public List<BorrowRecord> getBorrowHistoryByUser(Integer userId) {
        return borrowRecordRepository.findByUserIdOrderByBorrowDateDesc(userId);
    }

    @Override
    public BorrowRecord getBorrowRecordById(Integer recordId) {
        return borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Phiếu mượn với ID: " + recordId));
    }
}

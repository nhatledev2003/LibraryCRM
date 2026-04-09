package com.backend.librarycrm.service.impl;

import com.backend.librarycrm.repository.BookRepository;
import com.backend.librarycrm.repository.BorrowRecordRepository;
import com.backend.librarycrm.repository.UserRepository;
import com.backend.librarycrm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Override
    public Map<String, Object> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookRepository.count());
        stats.put("totalReaders", userRepository.countByRole("READER"));
        stats.put("activeBorrows", borrowRecordRepository.countByStatus("BORROWING"));

        // Tính tổng tiền phạt đang nợ trong toàn hệ thống
        Double totalFinePending = userRepository.findAll().stream()
                .mapToDouble(user -> user.getFineBalance() != null ? user.getFineBalance() : 0.0)
                .sum();
        stats.put("totalFinePending", totalFinePending);

        return stats;
    }
}
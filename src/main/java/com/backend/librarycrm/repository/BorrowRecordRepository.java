package com.backend.librarycrm.repository;

import com.backend.librarycrm.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {


    List<BorrowRecord> findByUserIdOrderByBorrowDateDesc(Integer userId);


    List<BorrowRecord> findByStatus(String status);

    // Custom Query: Tìm tất cả các phiếu mượn ĐÃ QUÁ HẠN (Hạn trả < Thời gian hiện tại VÀ trạng thái đang mượn)
    @Query("SELECT b FROM BorrowRecord b WHERE b.dueDate < CURRENT_TIMESTAMP AND b.status = 'BORROWING'")
    List<BorrowRecord> findOverdueRecords();


    long countByStatus(String status);
}
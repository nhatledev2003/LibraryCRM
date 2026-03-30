package com.backend.librarycrm.repository;

import com.backend.librarycrm.model.BookInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, String> {

    // Tìm tất cả các cuốn sách vật lý của 1 đầu sách đang ở trạng thái AVAILABLE
    List<BookInstance> findByBookIdAndStatus(Integer bookId, String status);
}
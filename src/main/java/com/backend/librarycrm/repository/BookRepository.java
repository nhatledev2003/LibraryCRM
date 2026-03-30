package com.backend.librarycrm.repository;

import com.backend.librarycrm.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Kiểm tra sách đã tồn tại chưa bằng ISBN
    boolean existsByIsbn(String isbn);

    // Tìm kiếm sách theo tên hoặc tên tác giả (Dùng cho thanh Search của Độc giả)
    // IgnoreCase giúp tìm kiếm không phân biệt hoa/thường
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}

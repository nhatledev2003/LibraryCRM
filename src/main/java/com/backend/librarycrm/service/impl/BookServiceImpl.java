package com.backend.librarycrm.service.impl;
import com.backend.librarycrm.model.Book;
import com.backend.librarycrm.repository.BookRepository;
import com.backend.librarycrm.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    // Nhờ @RequiredArgsConstructor của Lombok, Spring Boot sẽ tự động Inject (tiêm) BookRepository vào đây
    private final BookRepository bookRepository;

    @Override
    public Book getBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + id));
    }

    @Override
    public Book createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Mã ISBN này đã tồn tại trong hệ thống!");
        }

        book.setTotalQuantity(0);
        book.setAvailableQuantity(0);

        return bookRepository.save(book);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }
}
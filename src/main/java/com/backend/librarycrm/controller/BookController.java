package com.backend.librarycrm.controller;

import com.backend.librarycrm.dto.request.BookRequest;
import com.backend.librarycrm.dto.response.BaseResponse;
import com.backend.librarycrm.dto.response.BookResponse;
import com.backend.librarycrm.model.Book;
import com.backend.librarycrm.model.Category;
import com.backend.librarycrm.repository.CategoryRepository;
import com.backend.librarycrm.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryRepository categoryRepository;


    @PostMapping
    public ResponseEntity<BaseResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest request) {

        // Tìm Category từ DB dựa trên categoryId FE gửi lên
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thể loại với ID: " + request.getCategoryId()));

        // Chuyển Request DTO thành Entity
        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .category(category)
                .coverImageUrl(request.getCoverImageUrl())
                .build();

        // Gọi Service lưu vào DB
        Book savedBook = bookService.createBook(book);

        // Trả về Response DTO
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(savedBook), "Thêm sách mới thành công"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BookResponse>> getBookById(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(BaseResponse.success(mapToResponse(book), "Lấy thông tin sách thành công"));
    }


    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<BookResponse>>> searchBooks(@RequestParam(required = false) String keyword) {
        List<Book> books = bookService.searchBooks(keyword);

        // Dùng Stream API của Java để chuyển đổi cả List Entity sang List DTO
        List<BookResponse> responseList = books.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(BaseResponse.success(responseList, "Tìm kiếm thành công"));
    }


    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : "Chưa phân loại")
                .totalQuantity(book.getTotalQuantity())
                .availableQuantity(book.getAvailableQuantity())
                .build();
    }
}
package com.backend.librarycrm.service;

import com.backend.librarycrm.model.Book;

import java.util.List;

public interface BookService {
    Book getBookById(Integer id);
    Book createBook(Book book);
    List<Book> searchBooks(String keyword);
}

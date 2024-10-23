package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Book;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Book book);
    void deleteBook(Long id);
    Book getBookById(Long id);
    List<Book> getAllBooks();
    void markBookAsUnavailable(Long bookId);
    void markBookAsAvailable(Long bookId);
}

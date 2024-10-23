package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void markBookAsUnavailable(Long bookId) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setAvailable(false);
            bookRepository.save(book);
        }
    }

    @Override
    public void markBookAsAvailable(Long bookId) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setAvailable(true);
            bookRepository.save(book);
        }
    }
}


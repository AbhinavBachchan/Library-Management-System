package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Book;
import com.example.demo.model.Borrowing;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowingService;
import com.example.demo.exception.ResourceNotFoundException;


import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowingService borrowingService;

    // Get borrowing history by studentId
    @GetMapping("/{studentId}/borrowings")
    public ResponseEntity<List<Borrowing>> getBorrowingHistory(@PathVariable Long studentId) {
        List<Borrowing> borrowingHistory = borrowingService.getBorrowingHistoryByStudentId(studentId);
        if (borrowingHistory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(borrowingHistory);
        }
        return ResponseEntity.ok(borrowingHistory);
    }

    // Get all available books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(books);
        }
        return ResponseEntity.ok(books);
    }

    // Request to issue a book by student
    @PostMapping("/issuebook/{studentId}/{bookId}")
    public ResponseEntity<String> requestIssueBook(@PathVariable Long studentId, @PathVariable Long bookId) {
        try {
            String response = borrowingService.requestIssueBook(studentId, bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Request to return a book by student
    @PostMapping("/returnbook/{studentId}/{bookId}")
    public ResponseEntity<String> requestReturnBook(@PathVariable Long studentId, @PathVariable Long bookId) {
        try {
            String response = borrowingService.requestReturnBook(studentId, bookId);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

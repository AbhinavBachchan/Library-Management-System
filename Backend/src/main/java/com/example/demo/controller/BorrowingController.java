package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Book;
import com.example.demo.model.BookRequest;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowingService;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/librarian")
public class BorrowingController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BorrowingService borrowingService;

	@Autowired
	private BookService bookService;

	// Add a new book to the library
	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
		try {
			Book savedBook = bookRepository.save(book);
			return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new InvalidRequestException("Invalid book data");
		}
	}

	// Update existing book details
	@PutMapping("/update/{id}")
	public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) {
		if (!bookRepository.existsById(book.getId())) {
			throw new ResourceNotFoundException("Book not found with id: " + book.getId());
		}
		Book updatedBook = bookRepository.save(book);
		return new ResponseEntity<>(updatedBook, HttpStatus.OK);
	}

	// Delete a book by id
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		if (!bookRepository.existsById(id)) {
			throw new ResourceNotFoundException("Book not found with id: " + id);
		}
		bookRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Get all pending issue requests
	@GetMapping("/pendingissuerequests")
	public ResponseEntity<List<BookRequest>> getAllPendingIssueRequests() {
		List<BookRequest> requests = borrowingService.getAllPendingIssueRequests();
//        if (requests.isEmpty()) {
//            return new ResponseEntity<>(requests,Htt);
//        }
		return new ResponseEntity<>(requests, HttpStatus.OK);
	}

	// Get all pending return requests
	@GetMapping("/pendingreturnrequests")
	public ResponseEntity<List<BookRequest>> getAllPendingReturnRequests() {
		List<BookRequest> requests = borrowingService.getAllPendingReturnRequests();
		if (requests.isEmpty()) {
			throw new ResourceNotFoundException("No pending return requests found");
		}
		return new ResponseEntity<>(requests, HttpStatus.OK);
	}

	// Process book issue request
	@PostMapping("/issue/{requestId}")
	public ResponseEntity<String> processBookIssue(@PathVariable Long requestId) {
		String result = borrowingService.processBookIssue(requestId);
		if (result == null) {
			throw new ResourceNotFoundException("Issue request not found with id: " + requestId);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Process book return and calculate fine if overdue
	@PutMapping("/return/{borrowId}")
	public ResponseEntity<String> processBookReturn(@PathVariable Long borrowId) {
		String result = borrowingService.processBookReturn(borrowId);
		if (result == null) {
			throw new ResourceNotFoundException("Borrowing record not found with id: " + borrowId);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Check fine for a borrowing
	@GetMapping("/fine/{borrowId}")
	public ResponseEntity<Double> getFine(@PathVariable Long borrowId) {
		double fine = borrowingService.getFine(borrowId);
		return new ResponseEntity<>(fine, HttpStatus.OK);
	}

	// Get all books
	@GetMapping("/all")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/findbook")
	public ResponseEntity<Book> findBook(@PathVariable Long bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isEmpty()) {
			throw new ResourceNotFoundException("Book not found with id: " + bookId);
		}
		Book searchedBook = book.get();
		return new ResponseEntity<>(searchedBook, HttpStatus.OK);
	}

	// General exception handler for all exceptions
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handle specific exceptions like ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public final ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
		System.out.println();
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}

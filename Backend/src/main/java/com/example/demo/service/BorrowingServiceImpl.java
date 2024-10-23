package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.model.Borrowing;
import com.example.demo.model.BookRequest;
import com.example.demo.model.BookRequest.RequestType;
import com.example.demo.model.Student;
import com.example.demo.repository.BookRequestRepository;
import com.example.demo.repository.BorrowingRepository;
import com.example.demo.repository.StudentRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private static final double FINE_RATE = 1.0; // $1 per day for overdue books
    private static final int BORROW_PERIOD = 14; // 14 days borrow period
    

    @Autowired
    private BorrowingRepository borrowingRepository;
    
    @Autowired
    private BookRequestRepository bookRequestRepository;

    @Autowired
    private BookService bookService;  

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public double getFine(Long borrowId) {
        return borrowingRepository.findById(borrowId)
            .map(Borrowing::getFine)
            .orElse(0.0);
    }

    @Override
    public List<Borrowing> getBorrowingHistoryByStudentId(Long studentId) {
        return borrowingRepository.findByStudentId(studentId);
    }

    @Override
    public List<BookRequest> getAllPendingIssueRequests() {
    	 List<BookRequest> pendingRequests = bookRequestRepository.findByIsProcessedFalse();
    	 List<BookRequest> issueRequests = pendingRequests.stream()
    	            .filter(req -> req.getRequestType() == RequestType.ISSUE)
    	            .collect(Collectors.toList());
    	    pendingRequests.forEach(req -> System.out.println("Request ID: " + req.getRequestId() + ", Type: " + req.getRequestType()));
    	    return issueRequests;
    }
    
    @Override
    public List<BookRequest> getAllPendingReturnRequests() {
    	 List<BookRequest> pendingRequests = bookRequestRepository.findByIsProcessedFalse();
    	 List<BookRequest> returnRequests = pendingRequests.stream()
    	            .filter(req -> req.getRequestType() == RequestType.RETURN)
    	            .collect(Collectors.toList());
    	    return returnRequests;
    }

    @Override
    public String processBookIssue(Long requestId) {
    	//System.out.println(pendingIssueRequests+ "in process book issue");
    	List<BookRequest> list = getAllPendingIssueRequests();
//    	System.out.println(list.get(0).toString());
    	System.out.println(requestId);
    	Optional<BookRequest> optionalRequest = list.stream()
    		    .peek(req -> System.out.println("Filtering request with ID: " + req.getRequestId() + ", Type: " + req.getRequestType()))
    		    .filter(req -> requestId != null && requestId==req.getRequestId())
    		    .findFirst();


        if (optionalRequest.isEmpty()) {
            return "Request not found.";
        }

        BookRequest request = optionalRequest.get();
        Book book = bookService.getBookById(request.getBookId());

        // Check if the book is available
        if (book.getAvailableCopies() > 0) {
            Borrowing borrowing = new Borrowing();
            borrowing.setBook(book);
            borrowing.setStudent(studentRepository.findById(request.getStudentId()).orElse(null));
            borrowing.setBorrowDate(LocalDate.now());
            borrowing.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD));
            System.out.println("saving...");
            borrowingRepository.save(borrowing);

            // Update book availability
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookService.updateBook(book);

            // Mark the request as processed
            request.setProcessed(true);
            bookRequestRepository.delete(request);
            return "Book issued successfully.";
        } else {
            return "Book is not available.";
        }
    }

    @Override
    public String processBookReturn(Long requestId) {
    	System.out.println(requestId);
        Optional<BookRequest> optionalRequest = getAllPendingReturnRequests().stream()
                .filter(req -> req.getRequestId()==req.getRequestId())
                .filter(req->req.getRequestType()!=RequestType.ISSUE)
                .findFirst();

        if (optionalRequest.isEmpty()) {
            return "Request not found.";
        }

        BookRequest returnRequest = optionalRequest.get();
        System.out.println(returnRequest.getBookId());
        System.out.println(returnRequest.getStudentId()+"after optional");
        Book book = bookService.getBookById(returnRequest.getBookId());

        // Find the borrowing entry for this student and book
        Optional<Borrowing> borrowingOptional = borrowingRepository
            .findByBookIdAndStudentId(returnRequest.getBookId(), returnRequest.getStudentId());

        if (borrowingOptional.isPresent()) {
            Borrowing borrowing = borrowingOptional.get();
            borrowing.setReturnDate(LocalDate.now());

            // Calculate fine if overdue
            if (borrowing.getReturnDate().isAfter(borrowing.getDueDate())) {
                long overdueDays = ChronoUnit.DAYS.between(borrowing.getDueDate(), borrowing.getReturnDate());
                borrowing.setFine(overdueDays * FINE_RATE); 
            } else {
                borrowing.setFine(0); // No fine if returned on time
            }

            borrowingRepository.delete(borrowing);

            // Update book availability
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookService.updateBook(book);

            // Mark the request as processed
            returnRequest.setProcessed(true);
            bookRequestRepository.delete(returnRequest);
            return "Book returned successfully.";
        } else {
            return "No active borrowing record found for this book and student.";
        }
    }

    @Override
    public String requestIssueBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Book book = bookService.getBookById(bookId);

        if (student == null || book == null) {
            return "Invalid student or book.";
        }

        BookRequest borrowingRequest = new BookRequest(
        		null,
                studentId,
                bookId,
                student.getUsername(),
                book.getTitle(),
                false,
                RequestType.ISSUE
        );

        bookRequestRepository.save(borrowingRequest);
        System.out.println("Issue request added: " + borrowingRequest);
        return "Request for book issuance submitted successfully.";
    }

    @Override
    public String requestReturnBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Book book = bookService.getBookById(bookId);

        if (student == null || book == null) {
            return "Invalid student or book.";
        }

        BookRequest returnRequest = new BookRequest(
               null,
                studentId,
                bookId,
                student.getUsername(),
                book.getTitle(),
                false,
                RequestType.RETURN
        );

        bookRequestRepository.save(returnRequest);
        System.out.println("Issue request added: " + returnRequest);
        return "Request for book return submitted successfully.";
    }

	@Override
	public List<BookRequest> getAllPendingRequests() {
		// TODO Auto-generated method stub
		return null;
	}
}

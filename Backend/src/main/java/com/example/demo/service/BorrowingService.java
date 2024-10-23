package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Borrowing;
import com.example.demo.model.BookRequest;

public interface BorrowingService {
	double getFine(Long borrowId);

	List<BookRequest> getAllPendingRequests();

	String processBookIssue(Long requestId);

	String processBookReturn(Long requestId);

	String requestIssueBook(Long studentId, Long bookId);

	String requestReturnBook(Long studentId, Long bookId);

	List<Borrowing> getBorrowingHistoryByStudentId(Long studentId);

	List<BookRequest> getAllPendingIssueRequests();

	List<BookRequest> getAllPendingReturnRequests();
}

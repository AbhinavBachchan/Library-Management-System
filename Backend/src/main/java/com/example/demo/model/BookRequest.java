package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BookRequest {
    
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    private Long studentId;
    private Long bookId;
    private String studentName;
    private String bookTitle;
    private boolean isProcessed;
    
    @Enumerated(EnumType.STRING)
    private RequestType requestType;
    
    public BookRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


 public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	// Enum defined inside the class
	 public enum RequestType {
	        ISSUE,
	        RETURN
	    }

    // Constructors, Getters, and Setters
    public BookRequest(Long requestId, Long studentId, Long bookId, String studentName, String bookTitle, boolean isProcessed,RequestType requestType) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.studentName = studentName;
        this.bookTitle = bookTitle;
        this.isProcessed = isProcessed;
        this.requestType=requestType;
    }

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

    
    // Getters and setters...
}


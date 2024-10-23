package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Students")
public class Student extends MyUser {

	@Column(name = "BorrowedBook Count")
	private int currentBorrowedBooks;

	@Column(name = "Fine Amount")
	private double fines;

	public int getCurrentBorrowedBooks() {
		return currentBorrowedBooks;
	}

	public void setCurrentBorrowedBooks(int currentBorrowedBooks) {
		this.currentBorrowedBooks = currentBorrowedBooks;
	}

	public double getFines() {
		return fines;
	}

	public void setFines(double fines) {
		this.fines = fines;
	}
}

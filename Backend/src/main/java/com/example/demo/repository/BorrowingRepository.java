package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Borrowing;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

	List<Borrowing> findByStudentId(Long studentId);

	Optional<Borrowing> findByBookIdAndStudentId(Long bookId, Long studentId);
  
}

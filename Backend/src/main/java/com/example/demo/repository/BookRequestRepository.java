package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BookRequest;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
	 List<BookRequest> findByIsProcessedFalse();
	Optional<BookRequest> findByRequestIdAndIsProcessedFalse(Long requestId);
}

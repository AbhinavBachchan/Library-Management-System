package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
	Optional<MyUser> findByUsername(String username);
}

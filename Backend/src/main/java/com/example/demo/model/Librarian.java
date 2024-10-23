package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Librarians")
public class Librarian extends MyUser {
}

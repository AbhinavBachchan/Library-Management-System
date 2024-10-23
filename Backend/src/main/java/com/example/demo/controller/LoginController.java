package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Admin;
import com.example.demo.model.Librarian;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.MyUser;
import com.example.demo.model.Role;
import com.example.demo.model.Student;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.LibrarianRepository;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.JwtService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;

	@Autowired
	CustomUserDetailsService myUserDetailsService;

	@Autowired
	MyUserRepository myUserRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	LibrarianRepository librarianRepository;

	@Autowired
	AdminRepository adminRepository;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody MyUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user.getRole());
		// Check the user's role and save accordingly
		if (user.getRole().equals(Role.STUDENT)) {
			// Cast MyUser to Student and save in StudentRepository
			Student student = new Student();
			student.setUsername(user.getUsername());
			student.setPassword(user.getPassword());
			student.setRole(Role.STUDENT);
			// Set other attributes specific to Student
			studentRepository.save(student);
		} else if (user.getRole().equals(Role.ADMIN)) {
			// Save to AdminRepository
			Admin admin = new Admin();
			admin.setUsername(user.getUsername());
			admin.setPassword(user.getPassword());
			admin.setRole(Role.ADMIN);
			// Set other attributes specific to Admin
			adminRepository.save(admin);
		} else if (user.getRole().equals(Role.LIBRARIAN)) {
			// Save to LibrarianRepository
			Librarian librarian = new Librarian();
			librarian.setUsername(user.getUsername());
			librarian.setPassword(user.getPassword());
			librarian.setRole(Role.LIBRARIAN);
			// Set other attributes specific to Librarian
			librarianRepository.save(librarian);
		}

		return ResponseEntity.ok("User registered successfully!");
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateAndToken(@RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails);
			String token = jwtService.generateToken(userDetails);

			// Fetch user details
			MyUser user = myUserRepository.findByUsername(loginRequest.username())
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));

			// Create a response with token and user role
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("role", user.getRole());
            response.put("id", user.getId());
			return ResponseEntity.ok(response);

//	        throw new UsernameNotFoundException("Invalid Credentials");
//	    }
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
		}
	}

}

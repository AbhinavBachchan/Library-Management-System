package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.MyUser;
import com.example.demo.repository.MyUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private MyUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = userRepository.findByUsername(username);
		System.out.println(user.get().getUsername());
		System.out.println(user.get().getPassword());
		System.out.println(user.get().getRole());
		if (user.isPresent()) {
			var userObj = user.get();
			return User.builder().username(userObj.getUsername()).password(userObj.getPassword())
					.roles(getRole(userObj)).build();
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	private String getRole(MyUser userObj) {
		if (userObj.getRole() == null)
			return "STUDENT";
		return userObj.getRole().toString();
	}
}

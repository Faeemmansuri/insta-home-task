package com.instagram.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.instagram.entity.UserEntity;
import com.instagram.request.LoginRequest;
import com.instagram.request.RegisterRequest;
import com.instagram.response.LoginResponse;

public interface UserService extends UserDetailsService {
	
	void registerUser(RegisterRequest request);
	
	LoginResponse loginUser(LoginRequest request);
	
	public UserEntity findUserByEmail(String email) throws UsernameNotFoundException;

}

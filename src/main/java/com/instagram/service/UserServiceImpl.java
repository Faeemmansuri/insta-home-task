package com.instagram.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.entity.UserEntity;
import com.instagram.exception.BadRequestException;
import com.instagram.repository.UserRepository;
import com.instagram.request.LoginRequest;
import com.instagram.request.RegisterRequest;
import com.instagram.response.LoginResponse;
import com.instagram.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService {
	
	private static final String PASSWORD_VALIDATION_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*(\\W|_)).{8,}$";
	private static final String EMAIL_VALIDATION_PATTERN = "^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$";
	private static final String ROLE_USER = "ROLE_USER"; 
	
	@Autowired private JwtUtils jwtUtils;
	@Autowired private UserRepository userRepository; 
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;

	@Override
	public void registerUser(RegisterRequest request) {
		boolean isValidPassword = Pattern.matches(PASSWORD_VALIDATION_PATTERN, request.getPassword());
		if(!isValidPassword) {
			throw new BadRequestException("Password should be 8 character long.");
		}
		
		boolean isValidEmail = Pattern.matches(EMAIL_VALIDATION_PATTERN, request.getEmail());
		if(!isValidEmail) {
			throw new BadRequestException("Enter Valid Email...");
		}
		
		Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
		if(userOptional.isPresent()) {
			throw new BadRequestException("Email Already Exists!!");
		}
		
		UserEntity user = new UserEntity();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setCreatedOn(new Date(System.currentTimeMillis()));
		user = userRepository.save(user);
	}

	@Override
	public LoginResponse loginUser(LoginRequest request) {
		Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());
		userOptional.orElseThrow(() -> new BadRequestException("Invalid Email or password."));
		
		authenticate(request.getEmail(), request.getPassword());
		
		String jwtToken = jwtUtils.generateJwt(userOptional.get());

		return new LoginResponse(jwtToken);
	}	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity findUserByEmail = findUserByEmail(email);
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER));
		return new User(findUserByEmail.getEmail(), findUserByEmail.getPassword(), authorities);
	}
	
	private void authenticate(String username, String password) throws BadRequestException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new BadRequestException("User Is Disabled");
		} catch (BadCredentialsException e) {
			throw new BadRequestException("INVALID_CREDENTIALS", e);
		}
	}

	@Override
	public UserEntity findUserByEmail(String email) throws UsernameNotFoundException {
		Optional<UserEntity> userOptional = userRepository.findByEmail(email);
		userOptional.orElseThrow(() -> new UsernameNotFoundException("Invalid username/password"));
		return userOptional.get();
	}

}

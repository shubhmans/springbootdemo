package com.envision.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.envision.demo.constants.UserRoleType;
import com.envision.demo.dao.Role;
import com.envision.demo.dao.User;
import com.envision.demo.dtos.security.LoginRequest;
import com.envision.demo.dtos.security.LoginResponse;
import com.envision.demo.dtos.security.RegisterUserRequest;
import com.envision.demo.dtos.security.RegisterUserResponse;
import com.envision.demo.enums.ErrorCode;
import com.envision.demo.exception.CustomException;
import com.envision.demo.repository.RoleRepository;
import com.envision.demo.repository.UserRepository;
import com.envision.demo.security.JwtTokenUtil;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
    private PasswordEncoder encoder;
	
	@Autowired
	JwtTokenUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final String DEFAULT_USER_ROLE = UserRoleType.CUSTOMER;

	public LoginResponse login(LoginRequest request) {

		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			return new LoginResponse(user.getUsername(), accessToken);

		} catch (BadCredentialsException ex) {
			throw new CustomException("Invalid credentials", ErrorCode.ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@Transactional
	public RegisterUserResponse register(RegisterUserRequest request) {
		
		userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
			throw new CustomException("User with username exists",  ErrorCode.INVALID_ARGUMENT, HttpStatus.BAD_REQUEST);
		});
		
		User user = User.builder()
				.fullName(request.getFullName())
				.username(request.getUsername())
				.password(encoder.encode(request.getPassword()))
				.roles(getDefaultRole())
				.build();
		User savedUser = userRepository.save(user);
		
		return RegisterUserResponse.builder()
				.id(savedUser.getId())
				.username(savedUser.getUsername())
				.build();
	}
	
	private List<Role> getDefaultRole() {
		return roleRepository.findByRoleType(DEFAULT_USER_ROLE);
	}
}

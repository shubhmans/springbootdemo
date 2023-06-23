package com.envision.demo.dtos.security;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterUserRequest {
	
	@NotBlank
	@Length(min = 5, max = 50)
	private String username;
	
	@NotBlank
	@Length(min = 5, max = 25)
	private String fullName;
	
	@NotBlank
	@Length(min = 5, max = 10)
	private String password;
	
}

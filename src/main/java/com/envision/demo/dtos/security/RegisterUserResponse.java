package com.envision.demo.dtos.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterUserResponse {
	
	private Integer id;
	private String username;
}

package com.envision.demo.dtos.security;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	
    @NotNull 
    @Length(min = 5, max = 50)
    private String username;
     
    @NotNull 
    @Length(min = 5, max = 10)
    private String password;
 
}
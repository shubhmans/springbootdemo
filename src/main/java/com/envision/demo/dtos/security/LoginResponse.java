package com.envision.demo.dtos.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String email;
    private String accessToken;
 
    public LoginResponse() { }
     
    public LoginResponse(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }
 
}
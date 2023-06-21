package com.envision.demo.security;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.envision.demo.dao.Role;
import com.envision.demo.dao.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
     
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
     
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getUsername()))
                .setIssuer("CodeJava")
                .claim("roles", user.getRoles().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
                 
    }
    
	public boolean validateAccessToken(String token) {
		Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
		return true;
	}

	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public Set<Role> getRoles(String token) {
		String roles = (String) parseClaims(token).get("roles");
		String[] roleNames = roles.substring(1, roles.length() - 1).split(",");
		Set<Role> userRoles = new HashSet<>();
		Arrays.stream(roleNames).forEach(role -> userRoles.add(new Role(role)));
		return userRoles;
	}
     
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
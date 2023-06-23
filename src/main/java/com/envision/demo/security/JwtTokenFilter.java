package com.envision.demo.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.envision.demo.dao.Role;
import com.envision.demo.dao.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	
    @Autowired
    private JwtTokenUtil jwtUtil;
    
    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	
    	String token = null;
		try {
			if (!hasAuthorizationBearer(request)) {
			    filterChain.doFilter(request, response);
			    return;
			}
 
			token = getAccessToken(request);
 
			if (!jwtUtil.validateAccessToken(token)) {
			    filterChain.doFilter(request, response);
			    return;
			}
		} catch (ExpiredJwtException ex) {
			log.error("Auth token expired ", ex);
		} catch (IllegalArgumentException ex) {
			log.error("Auth token is null, empty or only whitespace ", ex);
		} catch (MalformedJwtException ex) {
			log.error("Auth token is invalid ", ex);
		} catch (UnsupportedJwtException ex) {
			log.error("Auth token is not supported ", ex);
		} catch (SignatureException ex) {
			log.error("Auth token signature validation failed ", ex);
		} catch (Exception ex) {
			log.error("Error validating auth token", ex);
		}
 
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
	}
 
    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }
 
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header.split(" ")[1].trim();
    }
 
	private void setAuthenticationContext(String token, HttpServletRequest request) {
		if (token == null) {
			return;
		}
		UserDetails userDetails = getUserDetails(token);

		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
 
    private UserDetails getUserDetails(String token) {
        String[] jwtSubject = jwtUtil.getSubject(token).split(",");
        
        User user = new User();
        user.setId(Integer.parseInt(jwtSubject[0]));
        user.setUsername(jwtSubject[1]);
        
        List<Role> roles = jwtUtil.getRoles(token);
         
        for (Role role : roles) {
            user.addRole(role);
        }
        return user;
    }
}
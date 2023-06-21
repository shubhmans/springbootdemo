package com.envision.demo.security;

import java.io.IOException;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.envision.demo.dao.Role;
import com.envision.demo.dao.User;
import com.envision.demo.enums.ErrorCode;

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
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response, FilterChain filterChain)
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
			handleJwtTokenError(response, "Auth token expired", ErrorCode.TOKEN_EXPIRED);
//			throw new CustomException(, ErrorCode.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
		} catch (IllegalArgumentException ex) {
//			throw new CustomException("Auth token is null, empty or only whitespace", ErrorCode.TOKEN_INVALID,
//					HttpStatus.UNAUTHORIZED);
		} catch (MalformedJwtException ex) {
//			handleJwtTokenError(response, "Auth is invalid", ErrorCode.TOKEN_INVALID);
			
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("error_code", "errorCode");
			jsonObject.put("message", "message");
			response.getWriter().print(jsonObject);
		} catch (UnsupportedJwtException ex) {
//			throw new CustomException("Auth token is not supported", ErrorCode.TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
		} catch (SignatureException ex) {
//			throw new CustomException("Auth token signature validation failed", ErrorCode.TOKEN_SIGNATURE_INVALID,
//					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			handleJwtTokenError(response, "Error validating auth token", ErrorCode.TOKEN_INVALID);
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
 
        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
 
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
 
    private UserDetails getUserDetails(String token) {
        User user = new User();
        String[] jwtSubject = jwtUtil.getSubject(token).split(",");
 
        user.setId(Integer.parseInt(jwtSubject[0]));
        user.setUsername(jwtSubject[1]);
        
        
        Set<Role> roles = jwtUtil.getRoles(token);
         
        for (Role role : roles) {
            user.addRole(role);
        }
        return user;
    }

	private void handleJwtTokenError(HttpServletResponse response, String message, ErrorCode errorCode) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("error_code", errorCode);
		jsonObject.put("message", message);
		response.getWriter().print(jsonObject);
	}
}
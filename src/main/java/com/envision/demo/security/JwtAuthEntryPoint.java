package com.envision.demo.security;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.envision.demo.enums.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Handle all the token related exceptions
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {

		logger.error("Unauthorized error. Message - {}", e.getMessage());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("error_code", ErrorCode.UNAUTHORIZED);
			jsonObject.put("message", "Access denied, invalid token!");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		response.getWriter().print(jsonObject);
	}
}
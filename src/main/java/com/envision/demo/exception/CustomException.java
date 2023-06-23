package com.envision.demo.exception;

import org.springframework.http.HttpStatus;

import com.envision.demo.enums.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String message;
	private final ErrorCode errorCode;
	private final HttpStatus httpStatus;

	@Override
	public String getMessage() {
		return this.message;
	}
}

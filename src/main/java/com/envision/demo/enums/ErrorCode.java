package com.envision.demo.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
	TOKEN_EXPIRED,
	TOKEN_INVALID,
	TOKEN_SIGNATURE_INVALID,
	INTERNAL_SERVER_ERROR,
	INVALID_ARGUMENT,
	ACCESS_DENIED,
	MISSING_PARAMETER,
	VALIDATION_EXCEPTION,
	NOT_FOUND,
	UNAUTHORIZED;
	
}

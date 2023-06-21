package com.envision.demo.exception;

import static java.util.Optional.ofNullable;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.envision.demo.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LogManager.getLogger();
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiCallError<String>> handleCustomException(HttpServletRequest request,
			CustomException ex) {
		//logger.error("NotFoundException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.status(ex.getHttpStatus())
				.body(new ApiCallError<>(ex.getErrorCode(), ex.getMessage()));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiCallError<String>> handleNotFoundException(HttpServletRequest request,
			NotFoundException ex) {
		logger.error("NotFoundException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiCallError<>(ErrorCode.NOT_FOUND, "Not found exception" + ex.getMessage()));
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiCallError<String>> handleValidationException(HttpServletRequest request,
			ValidationException ex) {
		logger.error("ValidationException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.badRequest().body(new ApiCallError<>(ErrorCode.VALIDATION_EXCEPTION,  "Validation exception"));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiCallError<String>> handleMissingServletRequestParameterException(
			HttpServletRequest request, MissingServletRequestParameterException ex) {
		logger.error("handleMissingServletRequestParameterException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.badRequest()
				.body(new ApiCallError<>(ErrorCode.MISSING_PARAMETER, "Missing required request parameter"));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiCallError<Map<String, String>>> handleMethodArgumentTypeMismatchException(
			HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
		logger.error("handleMethodArgumentTypeMismatchException {}\n", request.getRequestURI(), ex);

		String errorMessage = String.format("Invalid param '%s', %s", ex.getName(),
				ofNullable(ex.getValue()).map(Object::toString).orElse(""));

		return ResponseEntity.badRequest().body(new ApiCallError<>(ErrorCode.INVALID_ARGUMENT, errorMessage));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiCallError<Map<String, String>>> handleMethodArgumentNotValidException(
			HttpServletRequest request, MethodArgumentNotValidException ex) {
		logger.error("handleMethodArgumentNotValidException {}\n", request.getRequestURI(), ex);

		FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
		String errorMessage = String.format("Invalid argument for field '%s', %s", fieldError.getField(),
				fieldError.getDefaultMessage());

		return ResponseEntity.badRequest().body(new ApiCallError<>(ErrorCode.INVALID_ARGUMENT, errorMessage));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiCallError<String>> handleAccessDeniedException(HttpServletRequest request,
			AccessDeniedException ex) {
		logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ApiCallError<>(ErrorCode.ACCESS_DENIED, "Access denied !"));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiCallError<String>> handleInternalServerError(HttpServletRequest request, Exception ex) {
		logger.error("handleInternalServerError {}\n", request.getRequestURI(), ex);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiCallError<>(ErrorCode.INTERNAL_SERVER_ERROR, "Internal server error"));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ApiCallError<T> {
		
		private ErrorCode errorCode;
		private String message;
	}
}
package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.dto.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.DecodingException;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = RestController.class)
public class RestControllerGlobalExceptionHandler {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@ExceptionHandler(HttpClientErrorException.Forbidden.class)
	public ResponseEntity<ErrorDTO> forbidden() {
		logger.debug("You don't have permission to access this resource");
		return ResponseEntity.status(403).body(
			new ErrorDTO(403, "You don't have permission to access this resource")
		);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorDTO> notFound() {
		logger.debug("Resource not found");
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDTO> accessDenied() {
		logger.debug("You don't have permission to access this resource");
		return ResponseEntity.status(403).body(
			new ErrorDTO(403, "You don't have permission to access this resource")
		);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		logger.debug(e.getMessage());
		return ResponseEntity.status(405).body(
			new ErrorDTO(405, e.getMessage())
		);
	}

	@ExceptionHandler(ClientAbortException.class)
	public ResponseEntity<ErrorDTO> handleClientAbortException(ClientAbortException e) {
		logger.debug(e.getMessage());
		return ResponseEntity.status(500).body(
			new ErrorDTO(500, e.getMessage())
		);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		logger.debug(e.getMessage());
		return ResponseEntity.status(500).body(
			new ErrorDTO(500, e.getMessage())
		);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorDTO> handleExpiredJwtException() {
		logger.debug("Expired JWT Token");
		return ResponseEntity.status(403).body(
			new ErrorDTO(403, "Expired JWT Token")
		);
	}

	@ExceptionHandler(DecodingException.class)
	public ResponseEntity<ErrorDTO> handleDecodingException(DecodingException e) {
		logger.debug(e.getMessage());
		return ResponseEntity.status(500).body(
			new ErrorDTO(500, e.getMessage())
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> handleException(Exception e) {
		logger.error("Uncaught error: {}", e.getMessage());
		return ResponseEntity.status(500).body(
			new ErrorDTO(500, e.getMessage())
		);
	}
}

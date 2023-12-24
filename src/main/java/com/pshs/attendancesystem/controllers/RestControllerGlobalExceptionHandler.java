package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.dto.ErrorDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
		logger.info("You don't have permission to access this resource");
		return ResponseEntity.status(403).body(
			new ErrorDTO(403, "You don't have permission to access this resource")
		);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorDTO> notFound() {
		logger.info("Resource not found");
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDTO> accessDenied() {
		logger.info("You don't have permission to access this resource");
		return ResponseEntity.status(403).body(
			new ErrorDTO(403, "You don't have permission to access this resource")
		);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		logger.info(e.getMessage());
		return ResponseEntity.status(405).body(
			new ErrorDTO(405, e.getMessage())
		);
	}
}

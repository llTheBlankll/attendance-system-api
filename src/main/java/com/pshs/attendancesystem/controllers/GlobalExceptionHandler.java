package com.pshs.attendancesystem.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.codec.DecodingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException e) {
		return e.getMessage();
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFoundException(NoResourceFoundException e) {
		return "Invalid URL, please try again.";
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public String handleExpiredJwtException() {
		return "Your token has expired. Please login again.";
	}

	@ExceptionHandler(DecodingException.class)
	public String handleDecodingException(DecodingException e) {
		return "Your token is invalid. Please login again.";
	}

	@ExceptionHandler(ClientAbortException.class)
	public String handleClientAbortException(ClientAbortException e) {
		return e.getMessage();
	}
}

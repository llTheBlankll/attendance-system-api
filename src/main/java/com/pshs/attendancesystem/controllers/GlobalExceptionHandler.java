package com.pshs.attendancesystem.controllers;

import io.jsonwebtoken.ExpiredJwtException;
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
		return e.getMessage();
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public String handleExpiredJwtException(ExpiredJwtException e) {
		return "Your token has expired. Please login again.";
	}


}

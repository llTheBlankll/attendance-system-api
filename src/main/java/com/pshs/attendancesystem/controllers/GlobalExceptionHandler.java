package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.repositories.AttendanceRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.sentry.Sentry;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.codec.DecodingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

	private final AttendanceRepository attendanceRepository;

	public GlobalExceptionHandler(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException e) {
		return "error/403";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFoundException(NoResourceFoundException e) {
		return "error/404";
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public String handleExpiredJwtException() {
		return "error/403";
	}

	@ExceptionHandler(DecodingException.class)
	public String handleDecodingException(DecodingException e) {
		return "error/403";
	}

	@ExceptionHandler(ClientAbortException.class)
	public String handleClientAbortException(ClientAbortException e) {
		return "error/500";
	}

	@ExceptionHandler(NullPointerException.class)
	public String handleNullPointerException(NullPointerException e) {
		Sentry.captureException(e);
		return "error/500";
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		Sentry.captureException(e);
		return "error/500";
	}

	@GetMapping("/error/403")
	public String accessDenied() {
		return "error/forbidden";
	}

	@GetMapping("/error/404")
	public String notFound() {
		return "error/404";
	}

	@GetMapping("/error/500")
	public String serverError() {
		return "error/500";
	}
}

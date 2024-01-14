package com.pshs.attendancesystem.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.sentry.Sentry;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = Controller.class)
public class ControllerGlobalExceptionHandler {

	private final Logger logger = LogManager.getLogger(this.getClass());

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
		logger.error(e.getMessage(), e);
		return "error/403";
	}

	@ExceptionHandler(ClientAbortException.class)
	public String handleClientAbortException(ClientAbortException e, Model model) {
		model.addAttribute("message", e.getMessage());
		Sentry.captureException(e);
		logger.error(e.getMessage(), e);
		return "error/500";
	}

	@ExceptionHandler(NullPointerException.class)
	public String handleNullPointerException(NullPointerException e, Model model) {
		model.addAttribute("message", e.getMessage());
		Sentry.captureException(e);
		logger.error(e.getMessage(), e);
		return "error/500";
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, Model model) {
		model.addAttribute("message", e.getMessage());
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

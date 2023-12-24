package com.pshs.attendancesystem.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotAuthenticatedException extends AuthenticationException {

	public UserNotAuthenticatedException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

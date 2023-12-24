package com.pshs.attendancesystem.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotAuthenticated extends AuthenticationException {

	public UserNotAuthenticated(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

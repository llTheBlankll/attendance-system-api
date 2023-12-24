package com.pshs.attendancesystem.exception;

public class TooManyRequest extends RuntimeException {
	public TooManyRequest(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

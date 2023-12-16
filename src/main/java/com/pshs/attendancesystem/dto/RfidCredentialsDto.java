package com.pshs.attendancesystem.dto;

import jakarta.validation.constraints.NegativeOrZero;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.RfidCredentials}
 */
public class RfidCredentialsDto implements Serializable {
	@NegativeOrZero(message = "Cannot be negative or zero")
	private final Long lrn;
	private final StudentDto student;
	private final String hashedLrn;
	private final String salt;

	public RfidCredentialsDto(Long lrn, StudentDto student, String hashedLrn, String salt) {
		this.lrn = lrn;
		this.student = student;
		this.hashedLrn = hashedLrn;
		this.salt = salt;
	}

	public Long getLrn() {
		return lrn;
	}

	public StudentDto getStudent() {
		return student;
	}

	public String getHashedLrn() {
		return hashedLrn;
	}

	public String getSalt() {
		return salt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RfidCredentialsDto entity = (RfidCredentialsDto) o;
		return Objects.equals(this.lrn, entity.lrn) &&
			Objects.equals(this.student, entity.student) &&
			Objects.equals(this.hashedLrn, entity.hashedLrn) &&
			Objects.equals(this.salt, entity.salt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lrn, student, hashedLrn, salt);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"lrn = " + lrn + ", " +
			"student = " + student + ", " +
			"hashedLrn = " + hashedLrn + ", " +
			"salt = " + salt + ")";
	}
}
package com.pshs.attendancesystem.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Guardian}
 */
public class GuardianDto implements Serializable {
	private final Integer id;
	private final StudentDto student;
	private final String fullName;
	private final String contactNumber;

	public GuardianDto(Integer id, StudentDto student, String fullName, String contactNumber) {
		this.id = id;
		this.student = student;
		this.fullName = fullName;
		this.contactNumber = contactNumber;
	}

	public Integer getId() {
		return id;
	}

	public StudentDto getStudent() {
		return student;
	}

	public String getFullName() {
		return fullName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GuardianDto entity = (GuardianDto) o;
		return Objects.equals(this.id, entity.id) &&
			Objects.equals(this.student, entity.student) &&
			Objects.equals(this.fullName, entity.fullName) &&
			Objects.equals(this.contactNumber, entity.contactNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, student, fullName, contactNumber);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"student = " + student + ", " +
			"fullName = " + fullName + ", " +
			"contactNumber = " + contactNumber + ")";
	}
}
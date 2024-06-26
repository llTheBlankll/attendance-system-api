package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Student}
 */
public class StudentDto implements Serializable {
	private final Long lrn;
	private final String firstName;
	private final String middleName;
	private final String lastName;
	private final LocalDate birthdate;
	private final Gradelevel gradeLevel;
	private final String sex;
	private final Section section;
	private final Set<GuardianDto> guardian;
	private final String address;
	private final Set<AttendanceDto> attendances;

	public StudentDto(Long lrn, String firstName, String middleName, String lastName, LocalDate birthdate, Gradelevel gradeLevel, String sex, Section section, Set<GuardianDto> guardian, String address, Set<AttendanceDto> attendances) {
		this.lrn = lrn;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.gradeLevel = gradeLevel;
		this.sex = sex;
		this.section = section;
		this.guardian = guardian;
		this.address = address;
		this.attendances = attendances;
	}

	public Long getLrn() {
		return lrn;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public Gradelevel getGradeLevel() {
		return gradeLevel;
	}

	public String getSex() {
		return sex;
	}

	public Section getSection() {
		return section;
	}

	public Set<GuardianDto> getGuardian() {
		return guardian;
	}

	public String getAddress() {
		return address;
	}

	public Set<AttendanceDto> getAttendances() {
		return attendances;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StudentDto entity = (StudentDto) o;
		return Objects.equals(this.lrn, entity.lrn) &&
			Objects.equals(this.firstName, entity.firstName) &&
			Objects.equals(this.middleName, entity.middleName) &&
			Objects.equals(this.lastName, entity.lastName) &&
			Objects.equals(this.birthdate, entity.birthdate) &&
			Objects.equals(this.gradeLevel, entity.gradeLevel) &&
			Objects.equals(this.sex, entity.sex) &&
			Objects.equals(this.section, entity.section) &&
			Objects.equals(this.guardian, entity.guardian) &&
			Objects.equals(this.address, entity.address) &&
			Objects.equals(this.attendances, entity.attendances);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lrn, firstName, middleName, lastName, birthdate, gradeLevel, sex, section, guardian, address, attendances);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"lrn = " + lrn + ", " +
			"firstName = " + firstName + ", " +
			"middleName = " + middleName + ", " +
			"lastName = " + lastName + ", " +
			"birthdate = " + birthdate + ", " +
			"studentGradeLevel = " + gradeLevel + ", " +
			"sex = " + sex + ", " +
			"studentSection = " + section + ", " +
			"guardian = " + guardian + ", " +
			"address = " + address + ", " +
			"attendances = " + attendances + ")";
	}
}
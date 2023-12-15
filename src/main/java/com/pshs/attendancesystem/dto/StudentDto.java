package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Guardian;
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
	private final Gradelevel studentGradeLevel;
	private final String sex;
	private final Section studentSection;
	private final Set<Guardian> guardian;
	private final String address;
	private final Set<Attendance> attendances;

	public StudentDto(Long lrn, String firstName, String middleName, String lastName, LocalDate birthdate, Gradelevel studentGradeLevel, String sex, Section studentSection, Set<Guardian> guardian, String address, Set<Attendance> attendances) {
		this.lrn = lrn;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.studentGradeLevel = studentGradeLevel;
		this.sex = sex;
		this.studentSection = studentSection;
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

	public Gradelevel getStudentGradeLevel() {
		return studentGradeLevel;
	}

	public String getSex() {
		return sex;
	}

	public Section getStudentSection() {
		return studentSection;
	}

	public Set<Guardian> getGuardian() {
		return guardian;
	}

	public String getAddress() {
		return address;
	}

	public Set<Attendance> getAttendances() {
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
			Objects.equals(this.studentGradeLevel, entity.studentGradeLevel) &&
			Objects.equals(this.sex, entity.sex) &&
			Objects.equals(this.studentSection, entity.studentSection) &&
			Objects.equals(this.guardian, entity.guardian) &&
			Objects.equals(this.address, entity.address) &&
			Objects.equals(this.attendances, entity.attendances);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lrn, firstName, middleName, lastName, birthdate, studentGradeLevel, sex, studentSection, guardian, address, attendances);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"lrn = " + lrn + ", " +
			"firstName = " + firstName + ", " +
			"middleName = " + middleName + ", " +
			"lastName = " + lastName + ", " +
			"birthdate = " + birthdate + ", " +
			"studentGradeLevel = " + studentGradeLevel + ", " +
			"sex = " + sex + ", " +
			"studentSection = " + studentSection + ", " +
			"guardian = " + guardian + ", " +
			"address = " + address + ", " +
			"attendances = " + attendances + ")";
	}
}
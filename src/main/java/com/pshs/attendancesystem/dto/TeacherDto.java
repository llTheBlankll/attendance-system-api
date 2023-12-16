package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.entities.Section;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Teacher}
 */
public class TeacherDto implements Serializable {
	private final Integer id;
	private final String firstName;
	private final String middleName;
	private final String lastName;
	private final Set<Section> sections;

	public TeacherDto(Integer id, String firstName, String middleName, String lastName, Set<Section> sections) {
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.sections = sections;
	}

	public Integer getId() {
		return id;
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

	public Set<Section> getSections() {
		return sections;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TeacherDto entity = (TeacherDto) o;
		return Objects.equals(this.id, entity.id) &&
			Objects.equals(this.firstName, entity.firstName) &&
			Objects.equals(this.middleName, entity.middleName) &&
			Objects.equals(this.lastName, entity.lastName) &&
			Objects.equals(this.sections, entity.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, middleName, lastName, sections);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"firstName = " + firstName + ", " +
			"middleName = " + middleName + ", " +
			"lastName = " + lastName + ", " +
			"sections = " + sections + ")";
	}
}
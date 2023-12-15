package com.pshs.attendancesystem.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Gradelevel}
 */
public class GradelevelDto implements Serializable {
	private final Integer id;
	private final String gradeName;
	private final Set<SectionDto> sections;
	private final Set<StudentDto> students;

	public GradelevelDto(Integer id, String gradeName, Set<SectionDto> sections, Set<StudentDto> students) {
		this.id = id;
		this.gradeName = gradeName;
		this.sections = sections;
		this.students = students;
	}

	public Integer getId() {
		return id;
	}

	public String getGradeName() {
		return gradeName;
	}

	public Set<SectionDto> getSections() {
		return sections;
	}

	public Set<StudentDto> getStudents() {
		return students;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GradelevelDto entity = (GradelevelDto) o;
		return Objects.equals(this.id, entity.id) &&
			Objects.equals(this.gradeName, entity.gradeName) &&
			Objects.equals(this.sections, entity.sections) &&
			Objects.equals(this.students, entity.students);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, gradeName, sections, students);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"gradeName = " + gradeName + ", " +
			"sections = " + sections + ", " +
			"students = " + students + ")";
	}
}
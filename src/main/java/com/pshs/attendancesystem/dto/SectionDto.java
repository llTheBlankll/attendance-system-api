package com.pshs.attendancesystem.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Section}
 */
public class SectionDto implements Serializable {
	private final Integer sectionId;
	private final Integer room;
	private final GradelevelDto gradeLevel;
	private final String sectionName;
	private final TeacherDto teacher;
	private final Set<StudentDto> students;
	private final StrandDto strand;

	public SectionDto(Integer sectionId, Integer room, GradelevelDto gradeLevel, String sectionName, TeacherDto teacher, Set<StudentDto> students, StrandDto strand) {
		this.sectionId = sectionId;
		this.room = room;
		this.gradeLevel = gradeLevel;
		this.sectionName = sectionName;
		this.teacher = teacher;
		this.students = students;
		this.strand = strand;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public Integer getRoom() {
		return room;
	}

	public GradelevelDto getGradeLevel() {
		return gradeLevel;
	}

	public String getSectionName() {
		return sectionName;
	}

	public TeacherDto getTeacher() {
		return teacher;
	}

	public Set<StudentDto> getStudents() {
		return students;
	}

	public StrandDto getStrand() {
		return strand;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SectionDto entity = (SectionDto) o;
		return Objects.equals(this.sectionId, entity.sectionId) &&
			Objects.equals(this.room, entity.room) &&
			Objects.equals(this.gradeLevel, entity.gradeLevel) &&
			Objects.equals(this.sectionName, entity.sectionName) &&
			Objects.equals(this.teacher, entity.teacher) &&
			Objects.equals(this.students, entity.students) &&
			Objects.equals(this.strand, entity.strand);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sectionId, room, gradeLevel, sectionName, teacher, students, strand);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"sectionId = " + sectionId + ", " +
			"room = " + room + ", " +
			"gradeLevel = " + gradeLevel + ", " +
			"sectionName = " + sectionName + ", " +
			"teacher = " + teacher + ", " +
			"students = " + students + ", " +
			"strand = " + strand + ")";
	}
}
package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "gradelevels")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Gradelevel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grade_level", nullable = false)
	private Integer id;

	@Column(name = "grade_name", nullable = false)
	private String gradeName;

	@OneToMany(mappedBy = "gradeLevel", targetEntity = Section.class, cascade = CascadeType.ALL)
	private Set<Section> sections = new LinkedHashSet<>();

	@OneToMany(mappedBy = "gradeLevel", cascade = CascadeType.ALL)
	private Set<Student> students = new LinkedHashSet<>();

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

}
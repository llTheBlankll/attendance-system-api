package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gradelevels")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({
	"students"
})
public class Gradelevel implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grade_level", nullable = false)
	private Integer id;

	@Column(name = "grade_name", nullable = false)
	private String gradeName;

	@JoinColumn(name = "grade_strand")
	@ManyToOne(targetEntity = Strand.class, cascade = CascadeType.ALL)
	private Strand strand;

	@OneToMany(mappedBy = "gradeLevel", targetEntity = Section.class, cascade = CascadeType.ALL)
	private List<Section> sections = new ArrayList<>();

	@OneToMany(mappedBy = "gradeLevel", cascade = CascadeType.ALL)
	private List<Student> students = new ArrayList<>();

	public Strand getStrand() {
		return strand;
	}

	public void setStrand(Strand strand) {
		this.strand = strand;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
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
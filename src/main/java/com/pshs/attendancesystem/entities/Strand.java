package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "strand")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({
	"sections"
})
public class Strand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "strand_id", nullable = false)
	private Integer id;

	@Column(name = "strand_name", nullable = false)
	private String strandName;

	@OneToMany(mappedBy = "strand",targetEntity = Section.class, fetch = FetchType.EAGER)
	private List<Section> sections = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStrandName() {
		return strandName;
	}

	public void setStrandName(String strandName) {
		this.strandName = strandName;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

}
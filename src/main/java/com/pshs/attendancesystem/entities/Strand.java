package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "strand")
public class Strand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "strand_id", nullable = false)
	private Integer id;

	@Column(name = "strand_name", nullable = false)
	private String strandName;

	@OneToMany(mappedBy = "strand")
	@JsonBackReference
	private Set<Section> sections = new LinkedHashSet<>();

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

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

}
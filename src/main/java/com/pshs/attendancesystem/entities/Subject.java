package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "subjects")
public class Subject implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id", nullable = false)
	private Integer id;

	@Column(name = "name", length = 128)
	private String name;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
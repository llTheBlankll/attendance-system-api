package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "fingerprint")
public class Fingerprint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "fingerprint_id", nullable = false)
	private String fingerprintId;

	@OneToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	@JoinColumn(name = "student")
	private Student student;

	@Column(name = "template_data", length = Integer.MAX_VALUE)
	private String templateData;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFingerprintId() {
		return fingerprintId;
	}

	public void setFingerprintId(String fingerprintId) {
		this.fingerprintId = fingerprintId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

}
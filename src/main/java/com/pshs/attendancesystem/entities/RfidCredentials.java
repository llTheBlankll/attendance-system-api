package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "rfid_credentials", indexes = {
	@Index(columnList = "lrn")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lrn", scope = RfidCredentials.class)
@JsonIgnoreProperties({
	"salt"
})
public class RfidCredentials implements Serializable {
	@Id
	@Column(name = "lrn", nullable = false)
	private Long lrn;

	@Column(name = "hashed_lrn", length = 128)
	private String hashedLrn;

	@Column(name = "salt", length = 32)
	private String salt;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "lrn", referencedColumnName = "lrn", nullable = false)
	private Student student;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Long getLrn() {
		return lrn;
	}

	public void setLrn(Long lrn) {
		this.lrn = lrn;
	}

	public String getHashedLrn() {
		return hashedLrn;
	}

	public void setHashedLrn(String hashedLrn) {
		this.hashedLrn = hashedLrn;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
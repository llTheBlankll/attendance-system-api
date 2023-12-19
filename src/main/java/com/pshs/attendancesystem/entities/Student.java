package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "students", indexes = {
	@Index(columnList = "grade_level"),
	@Index(columnList = "section_id")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lrn")
@JsonIgnoreProperties({
	"rfid",
	"attendances"
})
public class Student {
	@Id
	@Column(name = "lrn", nullable = false)
	private Long lrn;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "birthdate")
	private LocalDate birthdate;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Gradelevel.class, cascade = CascadeType.DETACH)
	@JoinColumn(name = "grade_level")
	private Gradelevel gradeLevel;

	@Column(name = "sex", length = 6)
	private String sex;

	@ManyToOne(targetEntity = Section.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "section_id")
	private Section section;

	@OneToMany(mappedBy = "student", targetEntity = Guardian.class, fetch = FetchType.EAGER)
	private List<Guardian> guardian;

	@OneToOne(mappedBy = "student")
	private RfidCredentials rfid;

	@Column(name = "address", length = Integer.MAX_VALUE)
	private String address;

	@OneToMany(mappedBy = "student", cascade = CascadeType.DETACH, targetEntity = Attendance.class)
	private Set<Attendance> attendances;

	public RfidCredentials getRfid() {
		return rfid;
	}

	public void setRfid(RfidCredentials rfid) {
		this.rfid = rfid;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public List<Guardian> getGuardian() {
		return guardian;
	}

	public void setGuardian(List<Guardian> guardian) {
		this.guardian = guardian;
	}

	public Long getLrn() {
		return lrn;
	}

	public void setLrn(Long lrn) {
		this.lrn = lrn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gradelevel getGradeLevel() {
		return gradeLevel;
	}

	public void setGradeLevel(Gradelevel gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Attendance> getAttendances() {
		return attendances;
	}

	public void setAttendances(Set<Attendance> attendances) {
		this.attendances = attendances;
	}
}
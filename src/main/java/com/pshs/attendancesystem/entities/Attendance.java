package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pshs.attendancesystem.enums.Status;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "attendance", indexes = {
	@Index(name = "attendance_student_id_idx", columnList = "student_id")
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;
	@Enumerated(EnumType.STRING)
	private Status attendanceStatus;
	@Column(name = "date")
	private LocalDate date;
	@Column(name = "time")
	private Time time;
	@Column(name = "time_out")
	private Time timeOut;
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Status getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(Status attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public Time getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Time timeOut) {
		this.timeOut = timeOut;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
}
package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.enums.Status;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Attendance}
 */
public class AttendanceDto implements Serializable {
	private final Integer id;
	private final Status attendanceStatus;
	private final LocalDate date;
	private final Time time;
	private final Time timeOut;
	private final StudentDto student;

	public AttendanceDto(Integer id, Status attendanceStatus, LocalDate date, Time time, Time timeOut, StudentDto student) {
		this.id = id;
		this.attendanceStatus = attendanceStatus;
		this.date = date;
		this.time = time;
		this.timeOut = timeOut;
		this.student = student;
	}

	public Integer getId() {
		return id;
	}

	public Status getAttendanceStatus() {
		return attendanceStatus;
	}

	public LocalDate getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public Time getTimeOut() {
		return timeOut;
	}

	public StudentDto getStudent() {
		return student;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AttendanceDto entity = (AttendanceDto) o;
		return Objects.equals(this.id, entity.id) &&
			Objects.equals(this.attendanceStatus, entity.attendanceStatus) &&
			Objects.equals(this.date, entity.date) &&
			Objects.equals(this.time, entity.time) &&
			Objects.equals(this.timeOut, entity.timeOut) &&
			Objects.equals(this.student, entity.student);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, attendanceStatus, date, time, timeOut, student);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"attendanceStatus = " + attendanceStatus + ", " +
			"date = " + date + ", " +
			"time = " + time + ", " +
			"timeOut = " + timeOut + ", " +
			"student = " + student + ")";
	}
}
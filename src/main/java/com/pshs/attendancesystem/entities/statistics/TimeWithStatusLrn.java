package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class TimeWithStatusLrn {

	private String time;
	private Status status;
	private Long studentLrn;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getStudentLrn() {
		return studentLrn;
	}

	public void setStudentLrn(Long studentLrn) {
		this.studentLrn = studentLrn;
	}
}

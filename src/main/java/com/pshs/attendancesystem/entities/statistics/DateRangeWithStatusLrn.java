package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateRangeWithStatusLrn {

	private BetweenDate dateRange;
	private Status status;
	private Long studentLrn;

	public BetweenDate getDateRange() {
		return dateRange;
	}

	public void setDateRange(BetweenDate dateRange) {
		this.dateRange = dateRange;
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

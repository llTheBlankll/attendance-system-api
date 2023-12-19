package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateRangeWithStatusLrn {

	private DateRange dateRange;
	private Status status;
	private Long studentLrn;

	public DateRange getDateRange() {
		return dateRange;
	}

	public void setDateRange(DateRange dateRange) {
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

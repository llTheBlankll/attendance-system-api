package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateRangeWithStatus {

	public DateRangeWithStatus(DateRange dateRange, Status status) {
		this.dateRange = dateRange;
		this.status = status;
	}

	private DateRange dateRange;
	private Status status;

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
}

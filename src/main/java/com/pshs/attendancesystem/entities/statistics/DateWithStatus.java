package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateWithStatus {

	private BetweenDate dateRange;
	private Status status;

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
}

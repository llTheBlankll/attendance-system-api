package com.pshs.attendancesystem.entities.statistics;

import java.time.LocalDate;

public class BetweenDate {
	private LocalDate startDate;
	private LocalDate endDate;

	public BetweenDate(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public BetweenDate() {
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}

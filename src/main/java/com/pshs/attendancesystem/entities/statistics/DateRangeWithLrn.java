package com.pshs.attendancesystem.entities.statistics;

import java.time.LocalDate;

public class DateRangeWithLrn {

	private LocalDate startDate;
	private LocalDate endDate;
	private Long lrn;

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

	public Long getLrn() {
		return lrn;
	}

	public void setLrn(Long lrn) {
		this.lrn = lrn;
	}
}

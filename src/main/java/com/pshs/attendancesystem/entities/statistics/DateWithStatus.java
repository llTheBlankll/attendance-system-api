package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

import java.time.LocalDate;

public class DateWithStatus {
	private LocalDate date;
	private Status status;

	public DateWithStatus(LocalDate date, Status status) {
		this.date = date;
		this.status = status;
	}

	public DateWithStatus() {

	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}

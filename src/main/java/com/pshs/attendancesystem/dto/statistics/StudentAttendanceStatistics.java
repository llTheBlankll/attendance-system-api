package com.pshs.attendancesystem.dto.statistics;

public class StudentAttendanceStatistics {

	private Long totalAbsents;
	private Long totalOnTime;
	private Long totalLate;
	private Long totalPresent;
	private Double averageAttendancePercentage;

	public Long getTotalAbsents() {
		return totalAbsents;
	}

	public void setTotalAbsents(Long totalAbsents) {
		this.totalAbsents = totalAbsents;
	}

	public Long getTotalOnTime() {
		return totalOnTime;
	}

	public void setTotalOnTime(Long totalOnTime) {
		this.totalOnTime = totalOnTime;
	}

	public Long getTotalLate() {
		return totalLate;
	}

	public void setTotalLate(Long totalLate) {
		this.totalLate = totalLate;
	}

	public Long getTotalPresent() {
		return totalPresent;
	}

	public void setTotalPresent(Long totalPresent) {
		this.totalPresent = totalPresent;
	}

	public Double getAverageAttendancePercentage() {
		return averageAttendancePercentage;
	}

	public void setAverageAttendancePercentage(Double averageAttendancePercentage) {
		this.averageAttendancePercentage = averageAttendancePercentage;
	}
}

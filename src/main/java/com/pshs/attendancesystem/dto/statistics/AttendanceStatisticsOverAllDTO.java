package com.pshs.attendancesystem.dto.statistics;

public class AttendanceStatisticsOverAllDTO {

	private Long totalNumberOfStudents;
	private Double averageAttendancePercentage;
	private Long totalAbsents;
	private Long totalPresents;
	private Long totalLate;
	private Long totalOnTime;

	public Long getTotalNumberOfStudents() {
		return totalNumberOfStudents;
	}

	public void setTotalNumberOfStudents(Long totalNumberOfStudents) {
		this.totalNumberOfStudents = totalNumberOfStudents;
	}

	public Double getAverageAttendancePercentage() {
		return averageAttendancePercentage;
	}

	public void setAverageAttendancePercentage(Double averageAttendancePercentage) {
		this.averageAttendancePercentage = averageAttendancePercentage;
	}

	public Long getTotalAbsents() {
		return totalAbsents;
	}

	public void setTotalAbsents(Long totalAbsents) {
		this.totalAbsents = totalAbsents;
	}

	public Long getTotalPresents() {
		return totalPresents;
	}

	public void setTotalPresents(Long totalPresents) {
		this.totalPresents = totalPresents;
	}

	public Long getTotalLate() {
		return totalLate;
	}

	public void setTotalLate(Long totalLate) {
		this.totalLate = totalLate;
	}

	public Long getTotalOnTime() {
		return totalOnTime;
	}

	public void setTotalOnTime(Long totalOnTime) {
		this.totalOnTime = totalOnTime;
	}
}

package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;

import java.time.LocalDate;

public interface AttendanceService {

	Iterable<Attendance> getAllAttendances();

	Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(DateRange dateRange, Status status);

	Iterable<Attendance> getAllAttendanceBetweenDate(DateRange dateRange);

	Iterable<Attendance> getStudentAttendanceBetweenDateStatus(long studentLrn, DateRange dateRange, Status status);

	Iterable<Attendance> getAttendanceBetweenDate(long studentLrn, DateRange dateRange);

	Iterable<Attendance> getAttendanceInSectionByStatusBetweenDate(Integer sectionId, Status attendanceStatus, DateRange dateRange);

	Iterable<Attendance> getStudentAttendanceInSectionBetweenDate(Integer sectionId, DateRange dateRange);

	Iterable<Attendance> getAttendanceInSection(Integer sectionId, DateRange dateRange, Status status);

	Iterable<Attendance> getAttendanceInSection(Integer sectionId, DateRange dateRange);

	long countAttendanceInSection(Integer sectionId, DateRange dateRange, Status status);

	long countAttendanceInSectionByStatusAndDate(Integer sectionId, Status attendanceStatus, LocalDate date);

	long countAttendanceBySectionAndDate(Integer sectionId, LocalDate date);

	long getAllCountOfAttendanceBetweenDate(DateRange dateRange, Status status);

	long getAllCountOfAttendanceBetweenDate(long studentLrn, DateRange dateRange, Status status);

	long countAttendanceInSectionByStatusAndBetweenDate(Integer sectionId, Status attendanceStatus, DateRange dateRange);

	long countAttendanceBetweenDate(DateRange dateRange);

	long countStudentAttendanceBetweenDate(Long studentLrn, DateRange dateRange);

	long countByStudentStrandAndDate(Strand strand, LocalDate date, Status status);

	long countByStudentGradeLevelByStatusAndDate(Gradelevel gradeLevel, Status status, LocalDate date);

	boolean isLrnAndDateExist(Long studentLrn, LocalDate date);

	void setAsAbsent(Student student, LocalDate date);

	void absentAllNoAttendanceToday();

	Status createAttendance(Long studentLrn);

	String deleteAttendance(Integer attendanceId);

	String updateAttendance(Attendance attendance);

	Status getStatusToday(Long studentLrn);

	boolean attendanceOut(Long studentLrn);
	// getStatus(): Uses only the current time and takes it into account if the time is in within before the
	// Flag Ceremony or after the Flag Ceremony. Or if it is within the Late Time.
	Status getStatus();

	void deleteAllAttendance();

	void attendanceOut(Long studentLrn);

	boolean isAlreadyArrived(Long lrn);

	String deleteAllAttendance();
	Status isAlreadyOut(Long studentLrn);

	boolean isAttendanceExist(Integer attendanceId);
}
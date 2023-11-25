package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.BetweenDate;
import com.pshs.attendancesystem.enums.Status;

import java.time.LocalDate;

public interface AttendanceService {

	Iterable<Attendance> getAllAttendances();

	Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(LocalDate startDate, LocalDate endDate, Status status);

	Iterable<Attendance> getAllAttendanceBetweenDate(LocalDate startDate, LocalDate endDate);

	Iterable<Attendance> getStudentAttendanceBetweenDateWithAttendanceStatus(long studentLrn, BetweenDate dateRange, Status status);

	Iterable<Attendance> getAttendanceBetweenDate(long studentLrn, BetweenDate dateRange);

	Iterable<Attendance> getAttendanceInSectionId(Integer sectionId);

	Iterable<Attendance> getAttendanceInSectionByStatusBetweenDate(Integer sectionId, Status attendanceStatus, BetweenDate dateRange);

	Iterable<Attendance> getStudentAttendanceInSectionBetweenDate(Integer sectionId, BetweenDate betweenDate);

	Iterable<Attendance> getAttendanceInSectionByDate(Integer sectionId, LocalDate date);

	Iterable<Attendance> getAttendanceInSection(Integer sectionId, BetweenDate dateRange, Status status);

	Iterable<Attendance> getAttendanceInSection(Integer sectionId, BetweenDate dateRange);

	long countAttendanceInSection(Integer sectionId, BetweenDate dateRange, Status status);

	long countAttendanceInSectionByStatusAndDate(Integer sectionId, Status attendanceStatus, LocalDate date);

	long countAttendanceBySectionAndDate(Integer sectionId, LocalDate date);

	long getAllCountOfAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Status status);

	long getAllCountOfAttendanceBetweenDate(long studentLrn, BetweenDate dateRange, Status status);

	long countAttendanceInSectionByStatusAndBetweenDate(Integer sectionId, Status attendanceStatus, BetweenDate dateRange);

	long countAttendanceBetweenDate(BetweenDate dateRange);

	long countStudentAttendanceBetweenDate(Long studentLrn, BetweenDate dateRange);

	Status createAttendance(Long studentLrn);

	String deleteAttendance(Integer attendanceId);

	String updateAttendance(Attendance attendance);

	Status getAttendanceStatusToday(Long studentLrn);

	boolean attendanceOut(Long studentLrn);

	boolean checkIfAlreadyArrived(Student student);

	boolean checkIfAlreadyOut(Long studentLrn);

	String deleteAllAttendance();
}
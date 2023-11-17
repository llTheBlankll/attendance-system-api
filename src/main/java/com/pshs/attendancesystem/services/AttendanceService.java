package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;

import java.time.LocalDate;

public interface AttendanceService {

    Iterable<Attendance> getAllAttendances();

    Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(LocalDate startDate, LocalDate endDate, Status status);

    Iterable<Attendance> getAllAttendanceBetweenDate(LocalDate startDate, LocalDate endDate);

    Iterable<Attendance> getStudentAttendanceBetweenDateWithAttendanceStatus(long studentLrn
        , LocalDate startDate, LocalDate endDate, Status status);

    Iterable<Attendance> getStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate);

    Iterable<Attendance> getStudentAttendanceInSectionId(Integer sectionId);

    Iterable<Attendance> getStudentAttendanceInSectionIdByAttendanceStatusBetweenDate(Integer sectionId, Status attendanceStatus, LocalDate startDate, LocalDate endDate);


    long getAllCountOfAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Status status);

    long getAllCountOfStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

    long countStudentAttendanceInSectionIdByAttendanceStatusAndDate(Integer sectionId, Status attendanceStatus, LocalDate date);

    long countStudentAttendanceIBySectionIdByAttendanceStatusBetweenDate(Integer sectionId, Status attendanceStatus, LocalDate startDate, LocalDate endDate);

    long countStudentAttendanceInSectionByDate(Integer sectionId, LocalDate date);

    long countAttendancesBetweenDate(LocalDate startDate, LocalDate endDate);

    long countStudentAttendancesBetweenDate(Long studentLrn, LocalDate startDate, LocalDate endDate);

    Status createAttendance(Long studentLrn);

    String deleteAttendance(Integer attendanceId);

    String updateAttendance(Attendance attendance);

    Status getAttendanceStatusToday(Long studentLrn);

    boolean attendanceOut(Long studentLrn);

    boolean checkIfAlreadyArrived(Student student);

    boolean checkIfAlreadyOut(Long studentLrn);

    Attendance studentTodayAttendance(Long studentLrn);

    String deleteAllAttendance();
}
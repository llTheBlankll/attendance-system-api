package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    Iterable<Attendance> getAllAttendances();

    String createAttendance(Long studentLrn);

    String deleteAttendance(Integer attendanceId);

    String updateAttendance(Attendance attendance);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean existsByAttendanceId(Integer attendanceId);

    // * ATTENDANCE STATISTICS
    Iterable<Attendance> getStudentAttendanceToday(Long studentLrn);

    Iterable<Attendance> getLateStudentsThisMonth();

    Iterable<Attendance> getOnTimeStudentsThisMonth();

    Iterable<Attendance> getLateStudentsThisWeek();

    Iterable<Attendance> getOnTimeStudentsThisWeek();

    Iterable<Attendance> getLateStudentsToday();

    Iterable<Attendance> getOnTimeStudentsToday();

    long getLateStudentsCountThisMonth();

    long getOnTimeStudentsCountThisMonth();

    long getLateStudentsCountThisWeek();

    long getOnTimeStudentsCountThisWeek();

    long getLateStudentsCountToday();

    long getOnTimeStudentsCountToday();

    long getStudentAttendanceCountInSectionId(String sectionId, Status status, LocalDate date);

    long getStudentAttendanceCountInSectionIdBetweenDate(LocalDate startDate, LocalDate endDate, String sectionId, Status status);

    Iterable<Attendance> getStudentAttendanceBetweenDate(Long studentLrn, LocalDate startDate, LocalDate endDate);

    List<Student> getStudentAttendanceInSectionId(String sectionId);

    List<Student> getStudentsAttendanceInSectionIdByStatus(LocalDate startDate, LocalDate endDate, Status status, String sectionId);

}

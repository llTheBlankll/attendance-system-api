package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Attendance;

public interface AttendanceService {

    Iterable<Attendance> getAllAttendances();

    String createAttendance(Long studentLrn);

    String deleteAttendance(Integer attendanceId);

    String updateAttendance(Attendance attendance);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean existsByAttendanceId(Integer attendanceId);
}
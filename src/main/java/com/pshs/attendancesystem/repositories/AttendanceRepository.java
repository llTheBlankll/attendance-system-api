package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pshs.attendancesystem.Enums;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Iterable<Attendance> findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Enums.status attendanceStatus);
    long countByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Enums.status attendanceStatus);
    Iterable<Attendance> findByStudentLrnAndDate(Long studentLrn, LocalDate date);

    @Modifying
    @Query("UPDATE Attendance a SET a.timeOut = ?2 WHERE a.id = ?1")
    void studentAttendanceOut(LocalTime timeOut, Integer id);
}
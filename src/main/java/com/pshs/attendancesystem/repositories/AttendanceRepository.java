package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Iterable<Attendance> findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);
    Iterable<Attendance> findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);
    Iterable<Attendance> findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(Long studentLrn, LocalDate startDate, LocalDate endDate);
    Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionId(String sectionId);
    Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(String sectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

    long countByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);
    long countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);
    long countByStudent_StudentSection_SectionIdAndAttendanceStatusAndDate(String studentSectionId, Status status, LocalDate date);
    long countByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(String studentSectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

    /**
     * Updates the time when the time the student was out of the school.
     *
     * @param timeOut the new time out value
     * @param id      the ID of the attendance record
     */
    @Query("UPDATE Attendance attendance SET attendance.timeOut = ?1 WHERE attendance.id = ?2")
    @Modifying
    @Transactional
    void studentAttendanceOut(LocalTime timeOut, Integer id);
}
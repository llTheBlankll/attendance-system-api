package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Iterable<Attendance> findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);

    Iterable<Attendance> findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

    Iterable<Attendance> findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(Long studentLrn, LocalDate startDate, LocalDate endDate);

    Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionId(Integer sectionId);

    Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Integer sectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

    Iterable<Attendance> findAttendancesByDateGreaterThanEqualAndDateLessThanEqual(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Attendance a JOIN a.student s WHERE s.lrn = :studentLrn AND a.date = :date")
    Optional<Attendance> findByStudent_LrnAndDate(@Param("studentLrn") Long studentLrn, @Param("date") LocalDate date);

    long countByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);

    long countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

    long countByStudent_StudentSection_SectionIdAndAttendanceStatusAndDate(Integer studentSectionId, Status status, LocalDate date);

    long countByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(Integer studentSectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

    long countByDateGreaterThanEqualAndDateLessThanEqual(LocalDate startDate, LocalDate endDate);

    long countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(Long studentLrn, LocalDate startDate, LocalDate endDate);


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
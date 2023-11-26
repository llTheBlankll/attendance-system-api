package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

	Iterable<Attendance> findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);

	Iterable<Attendance> findByStudentLrnAndDateBetweenAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

	Iterable<Attendance> findByStudentLrnAndDateBetween(Long studentLrn, LocalDate startDate, LocalDate endDate);

	Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionId(Integer sectionId);

	Iterable<Attendance> findAttendancesByStudent_StudentSection_SectionIdAndDateBetweenAndAttendanceStatus(Integer sectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	Iterable<Attendance> findAttendancesByDateBetween(LocalDate startDate, LocalDate endDate);

	Iterable<Attendance> findByStudent_StudentSection_SectionIdAndDateBetween(Integer sectionId, LocalDate startDate, LocalDate endDate);

	@Query("SELECT a FROM Attendance a JOIN a.student s WHERE s.lrn = :studentLrn AND a.date = :date")
	Optional<Attendance> findByStudent_LrnAndDate(@Param("studentLrn") Long studentLrn, @Param("date") LocalDate date);

	long countByDateBetweenAndAttendanceStatus(LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	long countByStudentLrnAndDateBetweenAndAttendanceStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

	long countByStudent_StudentSection_SectionIdAndAttendanceStatusAndDate(Integer studentSectionId, Status status, LocalDate date);

	long countByStudent_StudentSection_SectionIdAndDateBetweenAndAttendanceStatus(Integer studentSectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	long countByDateBetween(LocalDate startDate, LocalDate endDate);

	long countByStudentLrnAndDateBetween(Long studentLrn, LocalDate startDate, LocalDate endDate);

	long countByStudent_StudentSection_SectionIdAndDateBetween(Integer studentSectionId, LocalDate startDate, LocalDate endDate);

	long countByStudent_StudentSection_SectionIdAndDate(Integer sectionId, LocalDate date);

	long countByStudent_StudentSection_StrandAndDateAndAttendanceStatus(Strand strand, LocalDate date, Status status);
	long countByStudent_StudentGradeLevelAndDateAndAttendanceStatus(Gradelevel studentGradeLevel, LocalDate date, Status status);

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
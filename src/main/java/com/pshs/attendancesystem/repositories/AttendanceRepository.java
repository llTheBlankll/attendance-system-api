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

	@Query("select a from Attendance a where a.date >= ?1 and a.date <= ?2 and a.attendanceStatus = ?3")
	Iterable<Attendance> searchBetweenDateAndStatus(LocalDate startdate, LocalDate endDate, Status attendanceStatus);

	@Query("select a from Attendance a where a.student.lrn = ?1 and a.date between ?2 and ?3 and a.attendanceStatus = ?4")
	Iterable<Attendance> searchLrnBetweenDateAndStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

	@Query("select a from Attendance a where a.student.lrn = ?1 and a.date between ?2 and ?3")
	Iterable<Attendance> searchLrnBetwenDate(Long studentLrn, LocalDate startDate, LocalDate endDate);

	@Query("select a from Attendance a where a.student.studentSection.sectionId = ?1")
	Iterable<Attendance> searchStudentSectionId(Integer sectionId);

	@Query("""
		select a from Attendance a
		where a.student.studentSection.sectionId = ?1 and a.date between ?2 and ?3 and a.attendanceStatus = ?4""")
	Iterable<Attendance> searchSectionIdBetweenDateAndStatus(Integer sectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	@Query("select a from Attendance a where a.date between ?1 and ?2")
	Iterable<Attendance> searchBetweenDate(LocalDate startDate, LocalDate endDate);

	@Query("select a from Attendance a where a.student.studentSection.sectionId = ?1 and a.date between ?2 and ?3")
	Iterable<Attendance> searchSectionIdBetweenDate(Integer sectionId, LocalDate startDate, LocalDate endDate);

	@Query("SELECT a FROM Attendance a JOIN a.student s WHERE s.lrn = :studentLrn AND a.date = :date")
	Optional<Attendance> findByStudent_LrnAndDate(@Param("studentLrn") Long studentLrn, @Param("date") LocalDate date);

	@Query("select count(a) from Attendance a where a.date between ?1 and ?2 and a.attendanceStatus = ?3")
	long countBetweenDateAndStatus(LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	@Query("""
		select count(a) from Attendance a
		where a.student.lrn = ?1 and a.date between ?2 and ?3 and a.attendanceStatus = ?4""")
	long countLrnBetweenDateAndStatus(Long studentLrn, LocalDate startDate, LocalDate endDate, Status status);

	@Query("""
		select count(a) from Attendance a
		where a.student.studentSection.sectionId = ?1 and a.attendanceStatus = ?2 and a.date = ?3""")
	long countSectionIdBetweenDateAndStatus(Integer studentSectionId, Status status, LocalDate date);

	@Query("""
		select count(a) from Attendance a
		where a.student.studentSection.sectionId = ?1 and a.date between ?2 and ?3 and a.attendanceStatus = ?4""")
	long countSectionIdBetweenDateAndStatus(Integer studentSectionId, LocalDate startDate, LocalDate endDate, Status attendanceStatus);

	@Query("select count(a) from Attendance a where a.date between ?1 and ?2")
	long countBetweenDate(LocalDate startDate, LocalDate endDate);

	@Query("select count(a) from Attendance a where a.student.lrn = ?1 and a.date between ?2 and ?3")
	long countLrnBetweenDate(Long studentLrn, LocalDate startDate, LocalDate endDate);

	@Query("select count(a) from Attendance a where a.student.studentSection.sectionId = ?1 and a.date between ?2 and ?3")
	long countSectionIdBetweenDate(Integer studentSectionId, LocalDate startDate, LocalDate endDate);

	@Query("select count(a) from Attendance a where a.student.studentSection.sectionId = ?1 and a.date = ?2")
	long countSectionIdAndDate(Integer sectionId, LocalDate date);

	@Query("""
		select count(a) from Attendance a
		where a.student.studentSection.strand = ?1 and a.date = ?2 and a.attendanceStatus = ?3""")
	long countSectionStrandAndStatus(Strand strand, LocalDate date, Status status);
	@Query("""
		select count(a) from Attendance a
		where a.student.studentGradeLevel = ?1 and a.date = ?2 and a.attendanceStatus = ?3""")
	long countStudentGradeLevelAndDateAndStatus(Gradelevel studentGradeLevel, LocalDate date, Status status);
	@Query("select (count(a) > 0) from Attendance a where a.student.lrn = ?1 and a.date = ?2")
	boolean isLrnAndDateExist(Long studentLrn, LocalDate date);

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
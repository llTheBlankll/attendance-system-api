package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findStudentByLrn(Long studentLrn);

	boolean existsById(Long id);

	@Query("select s from Student s where s.studentGradeLevel.gradeName = ?1")
	Iterable<Student> getStudentByGradeName(String gradeName);

	@Query("select (count(s) > 0) from Student s where s.studentGradeLevel.gradeName = ?1")
	boolean isGradeNameExist(String gradeName);

	@Query("select s from Student s where s.studentSection.sectionId = ?1")
	Iterable<Student> getStudentBySectionId(Integer sectionId);

	@Query("select count(s) from Student s where s.studentSection.sectionId = ?1")
	long countStudentsBySectionId(Integer sectionId);

	@Query("""
		select (count(s) > 0) from Student s inner join s.attendances attendances
		where s.lrn = ?1 and attendances.date = ?2""")
	boolean isStudentAttended(Long lrn, LocalDate date);
}
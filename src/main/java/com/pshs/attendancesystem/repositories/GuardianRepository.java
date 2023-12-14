package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Integer> {
	@Query("select g from Guardian g where g.student = ?1")
	Iterable<Guardian> getStudentGuardians(Student student);

	@Query("select g from Guardian g where g.student.lrn = ?1")
	Iterable<Guardian> getGuardiansByLrn(Long studentLrn);

	@Query("select g from Guardian g where upper(g.fullName) = upper(?1)")
	Iterable<Guardian> searchGuardiansByFullName(String fullName);

	@Query("select (count(g) > 0) from Guardian g where g.student.lrn = ?1")
	boolean isLrnExist(long studentLrn);
}
package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	@Query("""
		select t from Teacher t
		where t.firstName = ?1 and t.middleName like concat('%', ?2, '%') and t.lastName = ?3""")
	Optional<Teacher> findByTeacher(String firstName, String middleName, String lastName);
	@Query("select t from Teacher t where upper(t.lastName) = upper(?1)")
	Iterable<Teacher> searchByLastName(String lastName);

}
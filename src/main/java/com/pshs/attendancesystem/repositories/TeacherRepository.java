package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	Iterable<Teacher> findByLastNameIgnoreCase(String lastName);

	Page<Teacher> findAll(Pageable pageable);
}
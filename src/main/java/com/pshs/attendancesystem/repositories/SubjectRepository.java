package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
	@Query("select s from Subject s where upper(s.name) like upper(concat('%', ?1, '%'))")
	Iterable<Subject> searchSubjectsByName(String subjectName);

	@Query("select s from Subject s where upper(s.description) like upper(concat('%', ?1, '%'))")
	Iterable<Subject> searchSubjectsByDescription(String subjectDescription);

	@Query("select s from Subject s where upper(s.name) = upper(?1)")
	Subject getSubjectByName(String subjectName);
}
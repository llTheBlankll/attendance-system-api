package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
	Iterable<Subject> findSubjectsByNameIgnoreCaseContaining(String subjectName);

	Iterable<Subject> findSubjectsByDescriptionIgnoreCaseContaining(String subjectDescription);

	Subject findSubjectByNameIgnoreCase(String subjectName);
}
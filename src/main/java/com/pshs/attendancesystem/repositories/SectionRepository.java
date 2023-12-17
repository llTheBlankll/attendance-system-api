package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	@Query("select (count(s) > 0) from Section s where upper(s.teacher.lastName) like upper(concat('%', ?1, '%'))")
	boolean isTeacherLastNameExist(String lastName);

	Section findBySectionId(Integer sectionId);

	Optional<Section> findBySectionName(String sectionName);

	@Query("select s from Section s where upper(s.sectionName) like upper(concat('%', ?1, '%'))")
	Iterable<Section> searchSectionName(String sectionName);

	@Query("select s from Section s where upper(s.teacher.lastName) like upper(concat('%', ?1, '%'))")
	Iterable<Section> searchTeacherLastName(String lastName);
}
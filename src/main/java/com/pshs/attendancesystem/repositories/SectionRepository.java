package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
	Section findBySectionId(Integer sectionId);

	Optional<Section> findBySectionName(String sectionName);

	@Query("select s from Section s where upper(s.sectionName) like upper(concat('%', ?1, '%'))")
	Iterable<Section> searchSectionName(String sectionName);

	@Query("select s from Section s where s.teacher = ?1")
	List<Section> getAllSectionByTeacher(Teacher teacher);
}
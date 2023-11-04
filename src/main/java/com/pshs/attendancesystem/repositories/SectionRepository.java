package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, String> {
    boolean existsByTeacherLastNameIgnoreCaseContaining(String lastName);
    Section findBySectionId(String sectionId);

    Iterable<Section> findSectionsBySectionNameIgnoreCaseContaining(String sectionName);

    Iterable<Section> findByTeacherLastNameIgnoreCaseContaining(String lastName);
}
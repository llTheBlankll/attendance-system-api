package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Section;

public interface SectionService {
    Iterable<Section> getAllSection();

    String deleteSectionById(Integer id);

    String addSection(Section section);

    String updateSection(Section section);

    Iterable<Section> getSectionByTeacherLastName(String lastName);

    Section getSectionBySectionId(String sectionId);

    String deleteSection(Section section);

    Iterable<Section> searchSectionByName(String sectionName);
}

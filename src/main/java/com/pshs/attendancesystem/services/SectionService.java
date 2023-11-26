package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Section;

import java.util.List;

public interface SectionService {
	List<Section> getAllSection();

	String deleteSectionById(Integer id);

	String addSection(Section section);

	String updateSection(Section section);

	Iterable<Section> getSectionByTeacherLastName(String lastName);

	Section getSectionBySectionId(Integer sectionId);

	String deleteSection(Section section);

	Iterable<Section> searchSectionByName(String sectionName);
}

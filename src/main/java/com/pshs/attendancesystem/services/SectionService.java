package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Section;

import java.util.List;

public interface SectionService {
	List<Section> getAllSection();

	String deleteSectionById(Integer id);

	String addSection(Section section);

	String updateSection(Section section);

	Section getSectionBySectionId(Integer sectionId);

	String deleteSection(Section section);

	Iterable<Section> searchSectionByName(String sectionName);

	boolean isSectionExist(Section section);

	boolean isSectionExistById(Integer sectionId);
}

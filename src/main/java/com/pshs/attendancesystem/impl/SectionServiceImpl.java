package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.messages.SectionMessages;
import com.pshs.attendancesystem.repositories.SectionRepository;
import com.pshs.attendancesystem.services.SectionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SectionServiceImpl implements SectionService {
	private final SectionRepository sectionRepository;

	public SectionServiceImpl(SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	@Override
	public List<Section> getAllSection() {
		return this.sectionRepository.findAll();
	}

	@Override
	@CacheEvict(value = "section", key = "#id")
	public String deleteSectionById(Integer id) {
		if (id == null) {
			return SectionMessages.SECTION_NULL;
		}

		if (!this.sectionRepository.existsById(id)) {
			return SectionMessages.SECTION_NOT_FOUND;
		}

		this.sectionRepository.deleteById(id);
		return SectionMessages.SECTION_DELETED(id);
	}

	@Override
	@CachePut(value = "section", key = "#section.sectionId")
	public String addSection(Section section) {
		Optional<Section> databaseSection = this.sectionRepository.findBySectionName(section.getSectionName());
		if (databaseSection.isPresent()) {
			Section existingSection = databaseSection.get();
			if (existingSection.getSectionName().equals(section.getSectionName())) {
				return SectionMessages.SECTION_EXISTS;
			}
		}

		this.sectionRepository.save(section);
		return SectionMessages.SECTION_CREATED;
	}

	@Override
	@CachePut(value = "section", key = "#section.sectionId")
	public String updateSection(Section section) {
		if (section.getSectionId() == null) {
			return SectionMessages.SECTION_NULL;
		}

		if (!this.sectionRepository.existsById(section.getSectionId())) {
			return SectionMessages.SECTION_NOT_FOUND;
		}

		this.sectionRepository.save(section);
		return SectionMessages.SECTION_UPDATED;
	}

	@Override
	@Cacheable(value = "section", key = "#lastName")
	public Iterable<Section> getSectionByTeacherLastName(String lastName) {
		if (!this.sectionRepository.isTeacherLastNameExist(lastName)) {
			Stream<Section> emptyStream = Stream.empty();
			return emptyStream::iterator;
		}

		return this.sectionRepository.searchTeacherLastName(lastName);
	}

	@Override
	@Cacheable(value = "section", key = "#sectionId")
	public Section getSectionBySectionId(Integer sectionId) {
		return this.sectionRepository.findBySectionId(sectionId);
	}

	@Override
	@CacheEvict(value = "section", key = "#section.sectionId")
	public String deleteSection(Section section) {
		if (section.getSectionId() == null) {
			return SectionMessages.SECTION_NOT_FOUND;
		}

		if (!this.sectionRepository.existsById(section.getSectionId())) {
			return SectionMessages.SECTION_NOT_FOUND;
		}

		this.sectionRepository.delete(section);
		return SectionMessages.SECTION_DELETED(section.getSectionId());
	}

	@Override
	@Cacheable(value = "section", key = "#sectionName")
	public Iterable<Section> searchSectionByName(String sectionName) {
		return this.sectionRepository.searchSectionName(sectionName);
	}
}

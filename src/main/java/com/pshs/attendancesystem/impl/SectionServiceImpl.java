package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.messages.SectionMessages;
import com.pshs.attendancesystem.repositories.SectionRepository;
import com.pshs.attendancesystem.services.SectionService;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Iterable<Section> getAllSection() {
        return this.sectionRepository.findAll();
    }

    @Override
    public String deleteSectionById(String id) {
        if (!this.sectionRepository.existsById(id)) {
            return SectionMessages.SECTION_NOT_FOUND;
        }

        this.sectionRepository.deleteById(id);
        return SectionMessages.SECTION_DELETED(id);
    }

    @Override
    public String deleteSectionBySectionId(String sectionId) {
        if (sectionId.isEmpty()) {
            return SectionMessages.SECTION_CANNOT_EMPTY;
        }

        if (!this.sectionRepository.existsById(sectionId)) {
            return SectionMessages.SECTION_NOT_FOUND;
        }

        this.sectionRepository.deleteById(sectionId);
        return SectionMessages.SECTION_DELETED(sectionId);
    }

    @Override
    public String addSection(Section section) {
        if (this.sectionRepository.existsById(section.getSectionId())) {
            return SectionMessages.SECTION_EXISTS;
        }

        this.sectionRepository.save(section);
        return SectionMessages.SECTION_CREATED;
    }

    @Override
    public String updateSection(Section section) {
        if (!this.sectionRepository.existsById(section.getSectionId())) {
            return SectionMessages.SECTION_NOT_FOUND;
        }

        this.sectionRepository.save(section);
        return SectionMessages.SECTION_UPDATED;
    }

    @Override
    public Iterable<Section> getSectionByTeacherLastName(String lastName) {
        if (!this.sectionRepository.existsByTeacherLastName(lastName)) {
            Stream<Section> emptyStream = Stream.empty();
            return emptyStream::iterator;
        }

        return this.sectionRepository.findByTeacherLastNameIgnoreCase(lastName);
    }

    @Override
    public Section getSectionBySectionId(String sectionId) {
        return this.sectionRepository.findBySectionId(sectionId);
    }

    @Override
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
}

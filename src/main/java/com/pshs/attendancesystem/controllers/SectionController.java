package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.repositories.SectionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/section")
public class SectionController {

    private final SectionRepository sectionRepository;

    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/sections")
    public Iterable<Section> getAllSection() {
        return this.sectionRepository.findAll();
    }

    @GetMapping("/delete/{id}")
    public String deleteSectionById(@PathVariable String id) {
        if (!this.sectionRepository.existsById(id)) {
            return "Section does not exists.";
        }

        this.sectionRepository.deleteById(id);
        return "Section with ID " + id + " was deleted.";
    }

    @GetMapping("/delete")
    public void deleteSectionBySectionId(String sectionId) {
        this.sectionRepository.deleteSectionBySectionId(sectionId);
    }

    @PutMapping("/add")
    public String addSection(@RequestBody Section section) {
        if (this.sectionRepository.existsById(section.getSectionId())) {
            return "Section already exists";
        }

        this.sectionRepository.save(section);
        return "Section is created.";
    }

    @PostMapping("/update")
    public String updateSection(@RequestBody Section section) {
        if (!this.sectionRepository.existsById(section.getSectionId())) {
            return "Section does not exist.";
        }

        this.sectionRepository.save(section);
        return "Section was updated.";
    }

    // SEARCH FUNCTION

    @GetMapping("/search/adviser/{adviser}")
    public Iterable<Section> getSectionByAdviser(@PathVariable String adviser) {
        if (!this.sectionRepository.existsByAdviser(adviser)) return (Iterable<Section>) Collections.emptyIterator();
        return this.sectionRepository.findByAdviser(adviser);
    }
}

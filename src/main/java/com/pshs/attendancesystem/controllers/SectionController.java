package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.repositories.SectionRepository;
import org.springframework.web.bind.annotation.*;

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
    public void deleteSectionById(@PathVariable String id) {
        this.sectionRepository.deleteById(id);
    }

    @GetMapping("/delete")
    public void deleteSectionBySectionId(String sectionId) {
        this.sectionRepository.deleteSectionBySectionId(sectionId);
    }

    @PutMapping("/add")
    public void addSection(@RequestBody Section section) {
        this.sectionRepository.save(section);
    }

    @PostMapping("/update")
    public void updateSection(@RequestBody Section section) {
        this.sectionRepository.save(section);
    }

    // SEARCH FUNCTION

    @GetMapping("/search/adviser/{adviser}")
    public Iterable<Section> getSectionByAdviser(@PathVariable String adviser) {
        return this.sectionRepository.findByAdviser(adviser);
    }
}

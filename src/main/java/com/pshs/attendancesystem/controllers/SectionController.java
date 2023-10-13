package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.repositories.SectionRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

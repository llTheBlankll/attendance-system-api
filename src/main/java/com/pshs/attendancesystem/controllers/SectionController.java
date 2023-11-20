package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.services.SectionService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/section")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    /**
     * Retrieves all sections.
     *
     * @return an iterable collection of Section objects representing all sections
     */
    @GetMapping("/sections")
    public Iterable<Section> getAllSection() {
        return this.sectionService.getAllSection();
    }

    /**
     * Deletes a section by its ID.
     *
     * @param id the ID of the section to delete
     * @return a message indicating whether the section was deleted successfully or not
     */
    @GetMapping("/delete/id/{id}")
    public String deleteSectionById(@PathVariable Integer id) {
        return this.sectionService.deleteSectionById(id);
    }

    /**
     * Deletes a section by section ID.
     *
     * @param section the section object containing the section ID to be deleted
     * @return a message indicating whether the section was successfully deleted or not
     */
    @PostMapping("/delete")
    public String deleteSection(@RequestBody Section section) {
        return this.sectionService.deleteSection(section);
    }

    /**
     * Adds a new section to the repository.
     *
     * @param section the section to be added
     * @return a string indicating the status of the operation
     */
    @PostMapping("/create")
    public String addSection(@RequestBody Section section) {
        return this.sectionService.addSection(section);
    }

    /**
     * Updates a section.
     *
     * @param section the section to update
     * @return a message indicating the result of the update
     */
    @PostMapping("/update")
    public String updateSection(@RequestBody Section section) {
        return this.sectionService.updateSection(section);
    }

    // SEARCH FUNCTION
    @GetMapping("/search")
    public Iterable<Section> getSectionByAdviser(@RequestParam("type") String type, @RequestParam String search) {
        if (search.isEmpty()) {
            return Collections.emptyList();
        }

        if (type.equals("teacher")) {
            return this.sectionService.getSectionByTeacherLastName(search);
        } else if (type.equals("section name")) {
            return this.sectionService.searchSectionByName(search);
        }

        return Collections.emptyList();
    }

    @GetMapping("/get")
    public Section getSection(@RequestParam("sectionId") Integer sectionId) {
        return this.sectionService.getSectionBySectionId(sectionId);
    }
}

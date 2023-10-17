package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.repositories.SectionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/section")
public class SectionController {

    private final SectionRepository sectionRepository;

    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Retrieves all sections.
     *
     * @return  an iterable collection of Section objects representing all sections
     */
    @GetMapping("/sections")
    public Iterable<Section> getAllSection() {
        return this.sectionRepository.findAll();
    }

    /**
     * Deletes a section by its ID.
     *
     * @param  id  the ID of the section to delete
     * @return     a message indicating whether the section was deleted successfully or not
     */
    @GetMapping("/delete/id/{id}")
    public String deleteSectionById(@PathVariable String id) {
        if (!this.sectionRepository.existsById(id)) {
            return "Section does not exists.";
        }

        this.sectionRepository.deleteById(id);
        return "Section with ID " + id + " was deleted.";
    }

    /**
     * Deletes a section by section ID.
     *
     * @param  section  the section object containing the section ID to be deleted
     * @return          a message indicating whether the section was successfully deleted or not
     */
    @DeleteMapping("/delete")
    public String deleteSectionBySectionId(@RequestBody Section section) {
        if (section.getSectionId() == null) {
            return "Section does not exists.";
        }

        if (!this.sectionRepository.existsById(section.getSectionId())) {
            return "Section does not exists.";
        }

        this.sectionRepository.delete(section);
        return "Section with ID " + section.getSectionId() + " was deleted.";
    }

    /**
     * Adds a new section to the repository.
     *
     * @param  section  the section to be added
     * @return          a string indicating the status of the operation
     */
    @PutMapping("/add")
    public String addSection(@RequestBody Section section) {
        if (this.sectionRepository.existsById(section.getSectionId())) {
            return "Section already exists";
        }

        this.sectionRepository.save(section);
        return "Section is created.";
    }

    /**
     * Updates a section.
     *
     * @param  section  the section to update
     * @return          a message indicating the result of the update
     */
    @PostMapping("/update")
    public String updateSection(@RequestBody Section section) {
        if (!this.sectionRepository.existsById(section.getSectionId())) {
            return "Section does not exist.";
        }

        this.sectionRepository.save(section);
        return "Section was updated.";
    }

    // SEARCH FUNCTION

    /**
     * Retrieves a collection of Section objects based on the given adviser.
     *
     * @param  adviser  the adviser to search for
     * @return          an iterable collection of Section objects
     */
    @GetMapping("/search/adviser/{adviser}")
    public Iterable<Section> getSectionByAdviser(@PathVariable String adviser) {
        if (!this.sectionRepository.existsByAdviser(adviser)) {
            Stream<Section> emptyStream = Stream.empty();
            return emptyStream::iterator;
        }
        return this.sectionRepository.findByAdviserLikeIgnoreCase(adviser);
    }
}

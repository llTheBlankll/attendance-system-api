package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.messages.GradeLevelMessages;
import com.pshs.attendancesystem.repositories.GradeLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/gradelevel")
public class GradeLevelController {

    private final GradeLevelRepository gradelevelRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GradeLevelController(GradeLevelRepository gradelevelRepository) {
        this.gradelevelRepository = gradelevelRepository;
    }

    /**
     * Retrieves all grade levels from the database.
     *
     * @return an iterable collection of grade levels
     */
    @GetMapping("/gradelevels")
    public Iterable<Gradelevel> getAllGradeLevel() {
        return this.gradelevelRepository.findAll();
    }

    /**
     * Adds a grade level to the system.
     *
     * @param gradelevel the grade level to be added
     * @return a message indicating the status of the operation
     */
    @PostMapping("/create")
    public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (this.gradelevelRepository.existsById(gradelevel.getId())) {
            return GradeLevelMessages.GRADELEVEL_EXISTS;
        }

        this.gradelevelRepository.save(gradelevel);
        return GradeLevelMessages.GRADELEVEL_CREATED;
    }

    /**
     * Deletes a grade level.
     *
     * @param gradelevel the grade level object to be deleted
     * @return a message indicating the grade level was deleted
     */
    @PostMapping("/delete")
    public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (!this.gradelevelRepository.existsById(gradelevel.getId())) {
            logger.info(GradeLevelMessages.GRADELEVEL_NOT_FOUND);
            return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
        }

        this.gradelevelRepository.delete(gradelevel);
        return GradeLevelMessages.GRADELEVEL_DELETED;
    }

    /**
     * Deletes a grade level by its ID.
     *
     * @param id the ID of the grade level to be deleted
     * @return a message indicating whether the grade level was deleted or not
     */
    @PostMapping("/delete/{id}")
    public String deleteGradeLevelById(@PathVariable Integer id) {
        if (!this.gradelevelRepository.existsById(id)) {
            return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
        }

        this.gradelevelRepository.deleteById(id);
        return GradeLevelMessages.GRADELEVEL_DELETED;
    }

    /**
     * Update a grade level.
     *
     * @param gradelevel the grade level to update
     * @return a message indicating if the grade level was successfully updated or if it was empty
     */
    @PostMapping("/update")
    public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (gradelevel.getGradeName().isEmpty()) {
            return GradeLevelMessages.GRADELEVEL_EMPTY;
        }

        this.gradelevelRepository.save(gradelevel);
        return GradeLevelMessages.GRADELEVEL_UPDATED;
    }
}

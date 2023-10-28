package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Gradelevel;
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
     * @return  an iterable collection of grade levels
     */
    @GetMapping("/gradelevels")
    public Iterable<Gradelevel> getAllGradeLevel() {
        return this.gradelevelRepository.findAll();
    }

    /**
     * Adds a grade level to the system.
     *
     * @param  gradelevel  the grade level to be added
     * @return             a message indicating the status of the operation
     */
    @PostMapping("/add")
    public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (this.gradelevelRepository.existsById(gradelevel.getId())) {
            return "Grade level already exists";
        }
        this.gradelevelRepository.save(gradelevel);
        return "Grade level is created.";
    }

    /**
     * Deletes a grade level.
     *
     * @param  gradelevel    the grade level object to be deleted
     * @return               a message indicating the grade level was deleted
     */
    @PostMapping("/delete")
    public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (!this.gradelevelRepository.existsById(gradelevel.getId())) {
            logger.info("Grade level does not exist");
        }

        this.gradelevelRepository.delete(gradelevel);
        return "Grade level was deleted";
    }

    /**
     * Deletes a grade level by its ID.
     *
     * @param  id  the ID of the grade level to be deleted
     * @return     a message indicating whether the grade level was deleted or not
     */
    @PostMapping("/delete/{id}")
    public String deleteGradeLevelById(@PathVariable Integer id) {
        if (!this.gradelevelRepository.existsById(id)) {
            return "Grade level does not exist";
        }

        this.gradelevelRepository.deleteById(id);
        return "Grade level was deleted";
    }

    /**
     * Update a grade level.
     *
     * @param  gradelevel  the grade level to update
     * @return             a message indicating if the grade level was successfully updated or if it was empty
     */
    @PostMapping("/update")
    public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (gradelevel.getGradeName().isEmpty()) {
            return "Grade level is empty. Fill grade_level value.";
        }

        this.gradelevelRepository.save(gradelevel);
        return "Grade Level was updated.";
    }
}

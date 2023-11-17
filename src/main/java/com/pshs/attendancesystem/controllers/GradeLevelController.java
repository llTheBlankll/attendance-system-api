package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.services.GradeLevelService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/gradelevel")
public class GradeLevelController {

    private final GradeLevelService gradeLevelService;

    public GradeLevelController(GradeLevelService gradeLevelService) {
        this.gradeLevelService = gradeLevelService;
    }

    /**
     * Retrieves all grade levels from the database.
     *
     * @return an iterable collection of grade levels
     */
    @GetMapping("/gradelevels")
    public Iterable<Gradelevel> getAllGradeLevel() {
        return this.gradeLevelService.getAllGradeLevel();
    }

    /**
     * Adds a grade level to the system.
     *
     * @param gradelevel the grade level to be added
     * @return a message indicating the status of the operation
     */
    @PostMapping("/create")
    public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
        return this.gradeLevelService.addGradeLevel(gradelevel);
    }

    /**
     * Deletes a grade level.
     *
     * @param gradelevel the grade level object to be deleted
     * @return a message indicating the grade level was deleted
     */
    @PostMapping("/delete")
    public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
        return this.gradeLevelService.deleteGradeLevel(gradelevel);
    }

    /**
     * Deletes a grade level by its ID.
     *
     * @param id the ID of the grade level to be deleted
     * @return a message indicating whether the grade level was deleted or not
     */
    @PostMapping("/delete/{id}")
    public String deleteGradeLevelById(@PathVariable Integer id) {
        return this.gradeLevelService.deleteGradeLevelById(id);
    }

    @PostMapping("/update")
    public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
        return this.gradeLevelService.updateGradeLevel(gradelevel);
    }
}

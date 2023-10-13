package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.repositories.GradelevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/gradelevel")
public class GradeLevelController {

    private final GradelevelRepository gradelevelRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GradeLevelController(GradelevelRepository gradelevelRepository) {
        this.gradelevelRepository = gradelevelRepository;
    }

    @GetMapping("/gradelevels")
    public Iterable<Gradelevel> getAllGradeLevel() {
        return this.gradelevelRepository.findAll();
    }

    @PutMapping("/add")
    public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (this.gradelevelRepository.existsById(gradelevel.getId())) {
            return "Grade level already exists";
        }
        this.gradelevelRepository.save(gradelevel);
        return "Grade level is created.";
    }

    @DeleteMapping("/delete")
    public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (!this.gradelevelRepository.existsById(gradelevel.getId())) {
            logger.info("Grade level does not exist");
        }

        this.gradelevelRepository.delete(gradelevel);
        return "Grade level was deleted";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGradeLevelById(@PathVariable Integer id) {
        if (!this.gradelevelRepository.existsById(id)) {
            logger.info("Grade level does not exist");
        }

        this.gradelevelRepository.deleteById(id);
        return "Grade level was deleted";
    }

    @DeleteMapping("/update")
    public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
        if (gradelevel.getGradeName().isEmpty()) {
            return "Grade level is empty. Fill grade_level value.";
        }

        this.gradelevelRepository.save(gradelevel);
        return "Grade Level was updated.";
    }


}

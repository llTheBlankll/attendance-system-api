package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.repositories.GradelevelRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/gradelevel")
public class GradeLevelController {

    private final GradelevelRepository gradelevelRepository;

    public GradeLevelController(GradelevelRepository gradelevelRepository) {
        this.gradelevelRepository = gradelevelRepository;
    }

    @GetMapping("/gradelevels")
    public Iterable<Gradelevel> getAllGradeLevel() {
        return this.gradelevelRepository.findAll();
    }

    @PutMapping("/add")
    public void addGradeLevel(@RequestBody Gradelevel gradelevel) {
        this.gradelevelRepository.save(gradelevel);
    }

    @DeleteMapping("/delete")
    public void deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
        this.gradelevelRepository.delete(gradelevel);
    }


}

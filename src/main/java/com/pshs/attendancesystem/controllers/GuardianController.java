package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.GuardianService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/guardian")
@CrossOrigin
@RestController
public class GuardianController {

    private final GuardianService guardianService;

    public GuardianController(GuardianService guardianService) {
        this.guardianService = guardianService;
    }

    @GetMapping("/count")
    public long getGuardiansCount() {
        return guardianService.getGuardiansCount();
    }

    @GetMapping("/student/{lrn}")
    public Iterable<Guardian> getStudentGuardiansByLrn(@PathVariable Long lrn) {
        return guardianService.getStudentGuardiansByLrn(lrn);
    }

    @GetMapping("/search/last-name/{lastName}")
    public Iterable<Guardian> getGuardianByLastName(@PathVariable String lastName) {
        return guardianService.getGuardianByLastName(lastName);
    }

    @GetMapping("/student")
    public Iterable<Guardian> getGuardiansByStudent(@RequestBody Student student) {
        return guardianService.getStudentGuardians(student);
    }

    @PostMapping("/create")
    public boolean createGuardian(@RequestBody Guardian guardian) {
        return guardianService.createGuardian(guardian);
    }

    @PostMapping("/update")
    public boolean updateGuardian(@RequestBody Guardian guardian) {
        return guardianService.updateGuardian(guardian);
    }

    @DeleteMapping("/delete")
    public boolean deleteGuardian(@RequestBody Guardian guardian) {
        return guardianService.deleteGuardian(guardian);
    }

    @DeleteMapping("/delete/{guardianId}")
    public void deleteGuardianById(@PathVariable Integer guardianId) {
        guardianService.deleteGuardianById(guardianId);
    }
}

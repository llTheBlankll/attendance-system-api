package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.GuardianService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Iterable<Guardian> getStudentGuardiansByLrn(Long lrn) {
        return guardianService.getStudentGuardiansByLrn(lrn);
    }

    @GetMapping("/guardian/{lastName}")
    public Iterable<Guardian> getGuardianByLastName(String lastName) {
        return guardianService.getGuardianByLastName(lastName);
    }

    @GetMapping("/student")
    public Iterable<Guardian> getGuardiansByStudent(Student student) {
        return guardianService.getStudentGuardians(student);
    }
}

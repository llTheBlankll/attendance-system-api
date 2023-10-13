package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public Iterable<Student> getAllStudent() {
        return this.studentRepository.findAll();
    }
}

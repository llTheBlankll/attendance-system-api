package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalTime;

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

    @PutMapping("/add")
    public void addStudent(@RequestBody Student student) {
        this.studentRepository.save(student);
    }

    @DeleteMapping("/delete")
    public void deleteStudent(@RequestBody Student student) {
        this.studentRepository.delete(student);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        this.studentRepository.deleteById(id);
    }

    // SEARCH FUNCTION

    @GetMapping("/search/gradelevel/{gradelevel}")

    public Iterable<Student> getStudentByGradeLevel(@PathVariable String gradelevel) {
        return this.studentRepository.findStudentsByStudentGradeLevel_GradeName(gradelevel);
    }
}

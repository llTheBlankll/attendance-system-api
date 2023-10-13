package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collections;

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
    public String addStudent(@RequestBody Student student) {
        if (!this.studentRepository.existsById(student.getId())) {
            return "Student does not exist";
        }

        this.studentRepository.save(student);
        return "Student was added";
    }

    @DeleteMapping("/delete")
    public String deleteStudent(@RequestBody Student student) {
        if (!this.studentRepository.existsById(student.getId())) {
            return "Student does not exist";
        }

        this.studentRepository.delete(student);
        return "Student was deleted";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudentById(@PathVariable Long id) {
        if (!this.studentRepository.existsById(id)) {
            return "Student does not exist";
        }

        this.studentRepository.deleteById(id);
        return "Student was deleted";
    }

    // SEARCH FUNCTION

    @GetMapping("/search/gradelevel/name/{gradeName}")
    public Iterable<Student> getStudentByGradeLevel(@PathVariable("gradeName") String gradeName) {
        if (!this.studentRepository.existsByStudentGradeLevel_GradeName(gradeName)) {
            return (Iterable<Student>) Collections.emptyIterator();
        }

        return this.studentRepository.findStudentsByStudentGradeLevel_GradeName(gradeName);
    }

    @GetMapping("/search/id/{id}")
    public Student getStudentById(@PathVariable("id") Long id) {
        if (!this.studentRepository.existsById(id)) {
            return new Student();
        }

        return this.studentRepository.findStudentById(id);
    }
}

package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Teacher;
import com.pshs.attendancesystem.services.TeacherService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/all")
    public Iterable<Teacher> getAllTeachers() {
        return this.teacherService.getAllTeachers();
    }

    @GetMapping("/get/last-name/{last-name}")
    public Iterable<Teacher> getTeachersByLastName(@PathVariable("last-name") String lastName) {
        return this.teacherService.getTeacherByLastName(lastName);
    }

    @PostMapping("/create")
    public boolean createTeacher(@RequestBody Teacher teacher) {
        return this.teacherService.createTeacher(teacher);
    }

    @PostMapping("/update")
    public void updateTeacher(@RequestBody Teacher teacher) {
        this.teacherService.updateTeacher(teacher);
    }

    @PostMapping("/delete")
    public void deleteTeacher(@RequestBody Teacher teacher) {
        this.teacherService.deleteTeacher(teacher.getId());
    }

    @GetMapping("/get/{teacher-id}")
    public Teacher getTeacher(@PathVariable("teacher-id") Integer teacherId) {
        return this.teacherService.getTeacher(teacherId);
    }
}

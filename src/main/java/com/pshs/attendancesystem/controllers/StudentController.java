package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Retrieves all students.
     *
     * @return an iterable of student objects
     */
    @GetMapping("/students")
    public Iterable<Student> getAllStudent() {
        return this.studentService.getAllStudent();
    }

    /**
     * Adds a new student to the database.
     *
     * @param student the student object to be added
     * @return a message indicating the success of the operation
     */
    @PostMapping("/create")
    public String addStudent(@RequestBody Student student) {
        return this.studentService.addStudent(student);
    }

    /**
     * Deletes a student from the database.
     *
     * @param student the student object to be deleted
     * @return a string indicating the result of the deletion
     */
    @PostMapping("/delete")
    public String deleteStudent(@RequestBody Student student) {
        return this.studentService.deleteStudent(student);
    }

    /**
     * Deletes a student by their ID.
     *
     * @param id the ID of the student to delete
     * @return a message indicating if the student was deleted or if they do not exist
     */
    @PostMapping("/delete/lrn/{id}")
    public String deleteStudentById(@PathVariable Long id) {
        return this.studentService.deleteStudentById(id);
    }

    // SEARCH FUNCTION

    /**
     * Retrieves a list of students by grade level.
     *
     * @param gradeName the name of the grade level to search for
     * @return an iterable collection of Student objects
     */
    @GetMapping("/search/gradelevel/name/{gradeName}")
    public Iterable<Student> getStudentByGradeLevel(@PathVariable("gradeName") String gradeName) {
        return this.studentService.getStudentByGradeLevel(gradeName);
    }

    /**
     * Retrieves a student by their unique learning resource number (LRN).
     *
     * @param lrn the learning resource number of the student
     * @return the student with the specified LRN, or null if not found
     */
    @GetMapping("/search/lrn/{lrn}")
    public Student getStudentById(@PathVariable("lrn") Long lrn) {
        return this.studentService.getStudentById(lrn);
    }

    /**
     * Updates a student in the system.
     *
     * @param student the student object to be updated
     * @return a string indicating the result of the update
     */
    @PostMapping("/update")
    public String updateStudent(@RequestBody Student student) {
        return this.studentService.updateStudent(student);
    }

    @GetMapping("/get/students/{section_id}")
    public Iterable<Student> getAllStudentWithSectionId(@PathVariable("section_id") Integer sectionId) {
        return this.studentService.getAllStudentWithSectionId(sectionId);
    }

    @GetMapping("/count/students/{section_id}")
    public long countStudentsBySectionId(@PathVariable("section_id") Integer sectionId) {
        return this.studentService.countStudentsBySectionId(sectionId);
    }
}
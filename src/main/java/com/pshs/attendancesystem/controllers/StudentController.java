package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Scan;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.ScanRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final ScanRepository scanRepository;
    private final String salt = "ujsX54enWHyPuAU";

    /**
     * Hashes a given value using the MD5 algorithm.
     *
     * @param  value  the value to be hashed
     * @return        the hexadecimal string representation of the hash value
     */
    private String HashMD5(String value) {
        value += salt;
        try {
            // Create an instance of MessageDigest with MD5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the value to bytes
            byte[] valueBytes = value.getBytes();

            // Update the MessageDigest with the value bytes
            md.update(valueBytes);

            // Get the hash value as bytes
            byte[] hashBytes = md.digest();

            // Convert the hash bytes to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            // Return the hexadecimal string representation of the hash value
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception if MD5 algorithm is not available
            e.printStackTrace();
        }

        return null;
    }

    public StudentController(StudentRepository studentRepository, ScanRepository scanRepository) {
        this.studentRepository = studentRepository;
        this.scanRepository = scanRepository;
    }

    /**
     * Retrieves all students.
     *
     * @return  an iterable of student objects
     */
    @GetMapping("/students")
    public Iterable<Student> getAllStudent() {
        return this.studentRepository.findAll();
    }

    /**
     * Adds a new student to the database.
     *
     * @param  student  the student object to be added
     * @return          a message indicating the success of the operation
     */
    @PutMapping("/add")
    public String addStudent(@RequestBody Student student) {
        if (this.studentRepository.existsById(student.getLrn())) {
            return "Student already exists.";
        }

        Scan studentScan = new Scan();

        // First save the un-hashed student's learning resource number.
        this.studentRepository.save(student);

        // Then encode the student's learning resource number with MD5.
        studentScan.setLrn(student.getLrn());
        studentScan.setHashedLrn(HashMD5(student.getLrn().toString()));

        // Add the hashed lrn to the database.
        this.scanRepository.save(studentScan);

        return "Student was added";
    }

    /**
     * Deletes a student from the database.
     *
     * @param  student  the student object to be deleted
     * @return          a string indicating the result of the deletion
     */
    @DeleteMapping("/delete")
    public String deleteStudent(@RequestBody Student student) {
        if (!this.studentRepository.existsById(student.getLrn())) {
            return "Student does not exist";
        }

        this.studentRepository.delete(student);
        return "Student was deleted";
    }

    /**
     * Deletes a student by their ID.
     *
     * @param  id  the ID of the student to delete
     * @return     a message indicating if the student was deleted or if they do not exist
     */
    @DeleteMapping("/delete/lrn/{id}")
    public String deleteStudentById(@PathVariable Long id) {
        if (!this.studentRepository.existsById(id)) {
            return "Student does not exist";
        }

        this.studentRepository.deleteById(id);
        return "Student was deleted";
    }

    // SEARCH FUNCTION

    /**
     * Retrieves a list of students by grade level.
     *
     * @param  gradeName  the name of the grade level to search for
     * @return            an iterable collection of Student objects
     */
    @GetMapping("/search/gradelevel/name/{gradeName}")
    public Iterable<Student> getStudentByGradeLevel(@PathVariable("gradeName") String gradeName) {
        if (!this.studentRepository.existsByStudentGradeLevel_GradeName(gradeName)) {
            Stream<Student> empty = Stream.empty();
            return () -> empty.iterator(); // Return empty.
        }

        return this.studentRepository.findStudentsByStudentGradeLevel_GradeName(gradeName);
    }

    /**
     * Retrieves a student by their unique learning resource number (LRN).
     *
     * @param  lrn  the learning resource number of the student
     * @return      the student with the specified LRN, or null if not found
     */
    @GetMapping("/search/lrn/{lrn}")
    public Student getStudentById(@PathVariable("lrn") Long lrn) {
        if (!this.studentRepository.existsById(lrn)) {
            return null;
        }

        return this.studentRepository.findStudentByLrn(lrn);
    }

    /**
     * Updates a student in the system.
     *
     * @param  student  the student object to be updated
     * @return          a string indicating the result of the update
     */
    @PostMapping("/update")
    public String updateStudent(@RequestBody Student student) {
        if (!this.studentRepository.existsById(student.getLrn())) {
            return "Student does not exist.";
        }

        this.studentRepository.save(student);
        return "Student was updated.";
    }
}
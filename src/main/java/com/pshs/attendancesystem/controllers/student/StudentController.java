package com.pshs.attendancesystem.controllers.student;

import com.pshs.attendancesystem.documentation.StudentDocumentation;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Student", description = "The Student Endpoints")
@RestController
@RequestMapping("${api.root}/student")
@SecurityRequirement(
	name = "JWT Authentication"
)
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
	@Operation(
		summary = "Retrieves all students",
		description = StudentDocumentation.GET_ALL_STUDENTS
	)
	@GetMapping("/all")
	@Cacheable("student")
	public Iterable<Student> getAllStudent() {
		return studentService.getAllStudent();
	}

	/**
	 * Adds a new student to the database.
	 *
	 * @param student the student object to be added
	 * @return a message indicating the success of the operation
	 */
	@Operation(
		summary = "Add student",
		description = StudentDocumentation.CREATE_STUDENT
	)
	@PostMapping("/create")
	public String createStudent(@RequestBody Student student) {
		return studentService.createStudent(student);
	}

	/**
	 * Deletes a student from the database.
	 *
	 * @param student the student object to be deleted
	 * @return a string indicating the result of the deletion
	 */
	@Operation(
		summary = "Delete student",
		description = StudentDocumentation.DELETE_STUDENT
	)
	@PostMapping("/delete")
	public String deleteStudent(@RequestBody Student student) {
		return studentService.deleteStudent(student);
	}

	/**
	 * Deletes a student by their ID.
	 *
	 * @param lrn the ID of the student to delete
	 * @return a message indicating if the student was deleted or if they do not exist
	 */
	@Operation(
		summary = "Delete student by ID",
		description = StudentDocumentation.DELETE_STUDENT_BY_LRN
	)
	@PostMapping("/delete/lrn")
	public String deleteStudentById(@RequestParam("q") Long lrn) {
		return studentService.deleteStudentById(lrn);
	}

	// SEARCH FUNCTION

	/**
	 * Retrieves a list of students by grade level.
	 *
	 * @param gradeName the name of the grade level to search for
	 * @return an iterable collection of Student objects
	 */
	@Operation(
		summary = "Retrieves a list of students by grade level",
		description = StudentDocumentation.SEARCH_STUDENTS_BY_GRADE_LEVEL
	)
	@GetMapping("/search/gradelevel")
	public Iterable<Student> getStudentByGradeLevel(@RequestParam("q") String gradeName) {
		return studentService.getStudentByGradeLevel(gradeName);
	}

	/**
	 * Retrieves a student by their unique learning resource number (LRN).
	 *
	 * @param lrn the learning resource number of the student
	 * @return the student with the specified LRN, or null if not found
	 */
	@Operation(
		summary = "Retrieves a student by their learning resource number",
		description = StudentDocumentation.GET_STUDENT_BY_LRN
	)
	@GetMapping("/search/lrn")
	public Student getStudentById(@RequestParam("q") Long lrn) {
		return studentService.getStudentById(lrn);
	}

	/**
	 * Updates a student in the system.
	 *
	 * @param student the student object to be updated
	 * @return a string indicating the result of the update
	 */
	@Operation(
		summary = "Update student",
		description = StudentDocumentation.UPDATE_STUDENT
	)
	@PostMapping("/update")
	public String updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);
	}

	@Operation(
		summary = "Get all students with section id",
		description = "Get all students with section id",
		parameters = {
			@Parameter(name = "id", description = "The id of the section")
		}
	)
	@GetMapping("/get/students/section-id")
	public Iterable<Student> getAllStudentWithSectionId(@RequestParam("q") Integer sectionId) {
		return studentService.getAllStudentWithSectionId(sectionId);
	}

	@Operation(
		summary = "Count students by section id",
		description = "Count students by section id",
		parameters = {
			@Parameter(name = "id", description = "The id of the section")
		}
	)
	@GetMapping("/count/students/section-id")
	public long countStudentsBySectionId(@RequestParam("q") Integer sectionId) {
		return studentService.countStudentsBySectionId(sectionId);
	}

	@Operation(
		summary = "Search student by first name",
		description = StudentDocumentation.SEARCH_STUDENTS_BY_FIRST_NAME,
		parameters = {
			@Parameter(name = "name", description = "The first name of the student")
		}
	)
	@GetMapping("/search/firstname")
	public Iterable<Student> searchStudentByFirstName(@RequestParam("q") String name) {
		return studentService.searchStudentByFirstName(name);
	}

	@Operation(
		summary = "Search student by last name",
		description = StudentDocumentation.SEARCH_STUDENTS_BY_LAST_NAME,
		parameters = {
			@Parameter(name = "name", description = "The last name of the student")
		}
	)
	@GetMapping("/search/lastname")
	public Iterable<Student> searchStudentByLastName(@RequestParam("q") String name) {
		return studentService.searchStudentByLastName(name);
	}

	@Operation(
		summary = "Search student by first and last name",
		description = StudentDocumentation.SEARCH_STUDENTS_BY_FIRST_LAST_NAME,
		parameters = {
			@Parameter(name = "firstName", description = "The first name of the student"),
		}
	)
	@GetMapping("/search/first-last-name")
	public Iterable<Student> searchStudentByFirstAndLastName(@RequestParam("first") String firstName, @RequestParam("last") String lastName) {
		return studentService.searchStudentByFirstAndLastName(firstName, lastName);
	}

	@Operation(
		summary = "Check if LRN exist",
		description = StudentDocumentation.IS_LRN_EXIST,
		parameters = {
			@Parameter(name = "lrn", description = "The LRN of the student")
		}
	)
	@GetMapping("/is-lrn-exist")
	public boolean isLrnExist(@RequestParam("q") Long lrn) {
		return studentService.isLrnExist(lrn);
	}

	@Operation(
		summary = "Check if student attended",
		description = StudentDocumentation.IS_STUDENT_ATTENDED,
		parameters = {
			@Parameter(name = "lrn", description = "The LRN of the student"),
			@Parameter(name = "date", description = "The date of the attendance")
		}
	)
	@GetMapping("/is-student-attended")
	public boolean isStudentAttended(@RequestParam("lrn") Long lrn, @RequestParam("date") LocalDate date) {
		return studentService.isStudentAttended(lrn, date);
	}
}
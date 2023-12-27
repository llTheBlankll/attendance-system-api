package com.pshs.attendancesystem.controllers.teacher;

import com.pshs.attendancesystem.documentation.TeacherDocumentation;
import com.pshs.attendancesystem.entities.Teacher;
import com.pshs.attendancesystem.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Teacher", description = "The Teacher Endpoints")
@RestController
@RequestMapping("${api.root}/teacher")
@SecurityRequirement(
	name = "JWT Authentication"
)
public class TeacherController {

	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@Operation(
		summary = "Get all Teachers",
		description = TeacherDocumentation.GET_ALL_TEACHERS
	)
	@GetMapping("/all")
	public Iterable<Teacher> getAllTeachers() {
		return teacherService.getAllTeachers();
	}

	@Operation(
		summary = "Get Teacher by Last Name",
		description = TeacherDocumentation.GET_TEACHERS_BY_LAST_NAME,
		parameters = {
			@Parameter(name = "last-name", description = "The Last Name of the Teacher object to be retrieved")
		}
	)
	@GetMapping("/search/last-name")
	public Iterable<Teacher> getTeachersByLastName(@RequestParam("q") String lastName) {
		return teacherService.searchTeacherByLastName(lastName);
	}

	@Operation(
		summary = "Create Teacher",
		description = TeacherDocumentation.CREATE_TEACHER
	)
	@PostMapping("/create")
	public boolean createTeacher(@RequestBody Teacher teacher) {
		return teacherService.createTeacher(teacher);
	}

	@Operation(
		summary = "Update Teacher",
		description = TeacherDocumentation.UPDATE_TEACHER
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateTeacher(@RequestBody Teacher teacher) {
		return teacherService.updateTeacher(teacher);
	}

	@Operation(
		summary = "Delete Teacher",
		description = TeacherDocumentation.DELETE_TEACHER
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteTeacher(@RequestBody Teacher teacher) {
		return teacherService.deleteTeacher(teacher.getId());
	}

	@Operation(
		summary = "Get Teacher by ID",
		description = TeacherDocumentation.GET_TEACHER_BY_ID,
		parameters = {
			@Parameter(name = "teacher-id", description = "The ID of the Teacher object to be retrieved")
		}
	)
	@GetMapping("/id")
	public Teacher getTeacher(@RequestParam("q") Integer teacherId) {
		return teacherService.getTeacher(teacherId);
	}
}

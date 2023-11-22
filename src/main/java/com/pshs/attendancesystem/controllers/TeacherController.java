package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Teacher;
import com.pshs.attendancesystem.services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Teacher", description = "The Teacher Endpoints")
@RestController
@RequestMapping("/v1/teacher")
public class TeacherController {

	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@Operation(
		summary = "Get all Teachers",
		description = "Get all Teachers in the database."
	)
	@GetMapping("/all")
	public Iterable<Teacher> getAllTeachers() {
		return this.teacherService.getAllTeachers();
	}

	@Operation(
		summary = "Get Teacher by Last Name",
		description = "Get Teacher by Last Name in the database.",
		parameters = {
			@Parameter(name = "last-name", description = "The Last Name of the Teacher object to be retrieved")
		}
	)
	@GetMapping("/get/last-name/{last-name}")
	public Iterable<Teacher> getTeachersByLastName(@PathVariable("last-name") String lastName) {
		return this.teacherService.getTeacherByLastName(lastName);
	}

	@Operation(
		summary = "Create Teacher",
		description = "Create Teacher in the database.",
		parameters = {
			@Parameter(name = "teacher", description = "The Teacher object to be created")
		}
	)
	@PostMapping("/create")
	public boolean createTeacher(@RequestBody Teacher teacher) {
		return this.teacherService.createTeacher(teacher);
	}

	@Operation(
		summary = "Update Teacher",
		description = "Update Teacher in the database.",
		parameters = {
			@Parameter(name = "teacher", description = "The Teacher object to be updated")
		}
	)
	@PostMapping("/update")
	public void updateTeacher(@RequestBody Teacher teacher) {
		this.teacherService.updateTeacher(teacher);
	}

	@Operation(
		summary = "Delete Teacher",
		description = "Delete Teacher in the database.",
		parameters = {
			@Parameter(name = "teacher", description = "The Teacher object to be deleted")
		}
	)
	@PostMapping("/delete")
	public void deleteTeacher(@RequestBody Teacher teacher) {
		this.teacherService.deleteTeacher(teacher.getId());
	}

	@Operation(
		summary = "Get Teacher by ID",
		description = "Get Teacher by ID in the database.",
		parameters = {
			@Parameter(name = "teacher-id", description = "The ID of the Teacher object to be retrieved")
		}
	)
	@GetMapping("/get/{teacher-id}")
	public Teacher getTeacher(@PathVariable("teacher-id") Integer teacherId) {
		return this.teacherService.getTeacher(teacherId);
	}
}

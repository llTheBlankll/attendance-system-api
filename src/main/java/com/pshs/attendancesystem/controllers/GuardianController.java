package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.GuardianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Guardian", description = "The Guardian Endpoints")
@RequestMapping("/api/v1/guardian")
@RestController
public class GuardianController {

	private final GuardianService guardianService;

	public GuardianController(GuardianService guardianService) {
		this.guardianService = guardianService;
	}

	@Operation(
		summary = "Get Guardian Count",
		description = "Get Guardian Count in the database."
	)
	@GetMapping("/count")
	public long getGuardiansCount() {
		return guardianService.getGuardiansCount();
	}

	@Operation(
		summary = "Get Guardian By LRN",
		description = "Get the guardians of student by their LRN in the database.",
		parameters = {
			@Parameter(name = "lrn", description = "The LRN of the student")
		}
	)
	@GetMapping("/student/{lrn}")
	public Iterable<Guardian> getStudentGuardiansByLrn(@PathVariable Long lrn) {
		return guardianService.getStudentGuardiansByLrn(lrn);
	}

	@Operation(
		summary = "Get Guardian By Last Name",
		description = "Get Guardian by Last Name in the database.",
		parameters = {
			@Parameter(name = "last-name", description = "The Last Name of the Guardian object to be retrieved")
		}
	)
	@GetMapping("/search/last-name/{fullName}")
	public Iterable<Guardian> getGuardianByLastName(@PathVariable String fullName) {
		return guardianService.searchGuardianByFullName(fullName);
	}

	@Operation(
		summary = "Get Guardian by Student",
		description = "Get Guardian by Student in the database.",
		parameters = {
			@Parameter(name = "student", description = "The Student object to be retrieved")
		}
	)
	@GetMapping("/student")
	public Iterable<Guardian> getGuardiansByStudent(@RequestBody Student student) {
		return guardianService.getStudentGuardians(student);
	}

	@Operation(
		summary = "Create Guardian",
		description = "Create Guardian in the database.",
		parameters = {
			@Parameter(name = "guardian", description = "The Guardian object to be created")
		}
	)
	@PostMapping("/create")
	public boolean createGuardian(@RequestBody Guardian guardian) {
		return guardianService.createGuardian(guardian);
	}

	@Operation(
		summary = "Update Guardian",
		description = "Update Guardian in the database.",
		parameters = {
			@Parameter(name = "guardian", description = "The Guardian object to be updated")
		}
	)
	@PostMapping("/update")
	public boolean updateGuardian(@RequestBody Guardian guardian) {
		return guardianService.updateGuardian(guardian);
	}

	@Operation(
		summary = "Delete Guardian",
		description = "Delete Guardian in the database.",
		parameters = {
			@Parameter(name = "guardian", description = "The Guardian object to be deleted")
		}
	)

	@PostMapping("/delete")
	public boolean deleteGuardian(@RequestBody Guardian guardian) {
		return guardianService.deleteGuardian(guardian);
	}
	@Operation(
		summary = "Delete Guardian",
		description = "Delete Guardian in the database.",
		parameters = {
			@Parameter(name = "guardian", description = "The Guardian object to be deleted")
		}
	)

	@PostMapping("/delete/{guardianId}")
	public void deleteGuardianById(@PathVariable Integer guardianId) {
		guardianService.deleteGuardianById(guardianId);
	}
}

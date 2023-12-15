package com.pshs.attendancesystem.controllers.guardian;

import com.pshs.attendancesystem.documentation.GuardianDocumentation;
import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.GuardianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Guardian", description = "The Guardian Endpoints")
@RequestMapping("${api.root}/guardian")
@RestController
public class GuardianController {

	private final GuardianService guardianService;

	public GuardianController(GuardianService guardianService) {
		this.guardianService = guardianService;
	}

	@Operation(
		summary = "Get Guardian Count",
		description = GuardianDocumentation.GET_GUARDIAN_COUNT
	)
	@GetMapping("/count")
	public long getGuardiansCount() {
		return guardianService.getGuardiansCount();
	}

	@Operation(
		summary = "Get Guardian By LRN",
		description = GuardianDocumentation.GET_STUDENT_GUARDIANS_BY_LRN,
		parameters = {
			@Parameter(name = "lrn", description = "The LRN of the student")
		}
	)
	@GetMapping("/student/lrn")
	public Iterable<Guardian> getStudentGuardiansByLrn(@RequestParam("lrn") Long lrn) {
		return guardianService.getStudentGuardiansByLrn(lrn);
	}

	@Operation(
		summary = "Search Guardian By Full Name",
		description = GuardianDocumentation.SEARCH_GUARDIAN_BY_FULL_NAME,
		parameters = {
			@Parameter(name = "name", description = "The Full Name of the Guardian object to be retrieved")
		}
	)
	@GetMapping("/search/full-name")
	public Iterable<Guardian> getGuardianByLastName(@RequestParam("name") String fullName) {
		return guardianService.searchGuardianByFullName(fullName);
	}

	@Operation(
		summary = "Get Guardian by Student",
		description = GuardianDocumentation.GET_GUARDIANS_BY_STUDENT
	)
	@GetMapping("/student")
	public Iterable<Guardian> getGuardiansByStudent(@RequestBody Student student) {
		return guardianService.getStudentGuardians(student);
	}

	@Operation(
		summary = "Create Guardian",
		description = GuardianDocumentation.CREATE_GUARDIAN
	)
	@PostMapping("/create")
	public boolean createGuardian(@RequestBody Guardian guardian) {
		return guardianService.createGuardian(guardian);
	}

	@Operation(
		summary = "Update Guardian",
		description = GuardianDocumentation.UPDATE_GUARDIAN
	)
	@PostMapping("/update")
	public boolean updateGuardian(@RequestBody Guardian guardian) {
		return guardianService.updateGuardian(guardian);
	}

	@Operation(
		summary = "Delete Guardian",
		description = GuardianDocumentation.DELETE_GUARDIAN
	)

	@PostMapping("/delete")
	public boolean deleteGuardian(@RequestBody Guardian guardian) {
		return guardianService.deleteGuardian(guardian);
	}

	@Operation(
		summary = "Delete Guardian",
		description = GuardianDocumentation.DELETE_GUARDIAN_BY_ID,
		parameters = {
			@Parameter(name = "guardian", description = "The Guardian object to be deleted")
		}
	)
	@PostMapping("/delete/guardian-id")
	public String deleteGuardianById(@RequestParam("id") Integer guardianId) {
		return guardianService.deleteGuardianById(guardianId);
	}
}

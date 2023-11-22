package com.pshs.attendancesystem.controllers.gradelevel;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.services.GradeLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Grade Level", description = "Manage grade levels in the system")
@RestController
@RequestMapping("/v1/gradelevel")
public class GradeLevelController {

	private final GradeLevelService gradeLevelService;

	public GradeLevelController(GradeLevelService gradeLevelService) {
		this.gradeLevelService = gradeLevelService;
	}

	/**
	 * Retrieves all grade levels from the database.
	 *
	 * @return an iterable collection of grade levels
	 */
	@Operation(
		summary = "Retrieves all grade levels",
		description = "Retrieves all grade levels"
	)
	@GetMapping("/all")
	public Iterable<Gradelevel> getAllGradeLevel() {
		return this.gradeLevelService.getAllGradeLevel();
	}

	/**
	 * Adds a grade level to the system.
	 *
	 * @param gradelevel the grade level to be added
	 * @return a message indicating the status of the operation
	 */
	@Operation(
		summary = "Add grade level",
		description = "Add grade level to the system",
		parameters = {
			@Parameter(name = "gradelevel", description = "The grade level to be added")
		}
	)
	@PostMapping("/create")
	public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
		return this.gradeLevelService.addGradeLevel(gradelevel);
	}

	/**
	 * Deletes a grade level.
	 *
	 * @param gradelevel the grade level object to be deleted
	 * @return a message indicating the grade level was deleted
	 */
	@Operation(
		summary = "Delete grade level",
		description = "Delete grade level from the system",
		parameters = {
			@Parameter(name = "gradelevel", description = "The grade level object to be deleted")
		}
	)
	@PostMapping("/delete")
	public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
		return this.gradeLevelService.deleteGradeLevel(gradelevel);
	}

	/**
	 * Deletes a grade level by its ID.
	 *
	 * @param id the ID of the grade level to be deleted
	 * @return a message indicating whether the grade level was deleted or not
	 */
	@Operation(
		summary = "Delete grade level by ID",
		description = "Delete grade level by ID from the system"
	)
	@PostMapping("/delete/{id}")
	public String deleteGradeLevelById(@PathVariable Integer id) {
		return this.gradeLevelService.deleteGradeLevelById(id);
	}

	@Operation(
		summary = "Update grade level",
		description = "Update grade level in the database.",
		parameters = {
			@Parameter(name = "gradelevel", description = "The grade level object to be updated")
		}
	)
	@PostMapping("/update")
	public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
		return this.gradeLevelService.updateGradeLevel(gradelevel);
	}
}

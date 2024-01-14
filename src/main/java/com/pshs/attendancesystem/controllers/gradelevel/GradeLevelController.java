package com.pshs.attendancesystem.controllers.gradelevel;

import com.pshs.attendancesystem.documentation.GradeLevelDocumentation;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.services.GradeLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Grade Level", description = "Manage grade levels in the system")
@RestController
@RequestMapping("${api.root}/gradelevel")
@SecurityRequirement(
	name = "JWT Authentication"
)
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
		description = GradeLevelDocumentation.GET_ALL_GRADE_LEVELS
	)
	@GetMapping("/all")
	public Iterable<Gradelevel> getAllGradeLevel() {
		return gradeLevelService.getAllGradeLevel();
	}

	/**
	 * Adds a grade level to the system.
	 *
	 * @param gradelevel the grade level to be added
	 * @return a message indicating the status of the operation
	 */
	@Operation(
		summary = "Create grade level",
		description = GradeLevelDocumentation.CREATE_GRADELEVEL,
		parameters = {
			@Parameter(name = "gradelevel", description = "The grade level to be added")
		}
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String addGradeLevel(@RequestBody Gradelevel gradelevel) {
		return gradeLevelService.addGradeLevel(gradelevel);
	}

	/**
	 * Deletes a grade level.
	 *
	 * @param gradelevel the grade level object to be deleted
	 * @return a message indicating the grade level was deleted
	 */
	@Operation(
		summary = "Delete grade level",
		description = GradeLevelDocumentation.DELETE_GRADELEVEL,
		parameters = {
			@Parameter(name = "gradelevel", description = "The grade level object to be deleted")
		}
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteGradeLevel(@RequestBody Gradelevel gradelevel) {
		return gradeLevelService.deleteGradeLevel(gradelevel);
	}

	/**
	 * Deletes a grade level by its ID.
	 *
	 * @param id the ID of the grade level to be deleted
	 * @return a message indicating whether the grade level was deleted or not
	 */
	@Operation(
		summary = "Delete grade level by ID",
		description = GradeLevelDocumentation.DELETE_GRADELEVEL_BY_ID
	)
	@PostMapping(value = "/delete/id", produces = "text/plain")
	public String deleteGradeLevelById(@RequestParam("q") Integer id) {
		return gradeLevelService.deleteGradeLevelById(id);
	}

	@Operation(
		summary = "Update grade level",
		description = GradeLevelDocumentation.UPDATE_GRADELEVEL
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateGradeLevel(@RequestBody Gradelevel gradelevel) {
		return gradeLevelService.updateGradeLevel(gradelevel);
	}

	@Operation(
		summary = "Search grade level by name",
		description = GradeLevelDocumentation.SEARCH_GRADELEVEL_BY_NAME,
		parameters = {
			@Parameter(name = "name", description = "The name of the grade level to be searched")
		}
	)
	@GetMapping("/search/name")
	public Iterable<Gradelevel> searchGradeLevelByName(@RequestParam("q") String name) {
		return gradeLevelService.searchGradeLevelByName(name);
	}

	@PostMapping("/assign/strand")
	public String updateGradeLevelWithStrand(@RequestBody Gradelevel gradelevel, @RequestParam("q") Integer strandId) {
		return gradeLevelService.updateGradeLevelWithStrand(gradelevel, strandId);
	}
}

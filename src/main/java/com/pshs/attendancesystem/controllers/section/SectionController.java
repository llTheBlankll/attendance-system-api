package com.pshs.attendancesystem.controllers.section;

import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.services.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(name = "Section", description = "The Section Endpoints")
@RestController
@RequestMapping("${api.root}/section")
public class SectionController {

	private final SectionService sectionService;

	public SectionController(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	/**
	 * Retrieves all sections.
	 *
	 * @return an iterable collection of Section objects representing all sections
	 */
	@Operation(
		summary = "Retrieves all sections",
		description = "Retrieves all sections"
	)
	@GetMapping("/all")
	public Iterable<Section> getAllSection() {
		return sectionService.getAllSection();
	}

	/**
	 * Deletes a section by its ID.
	 *
	 * @param id the ID of the section to delete
	 * @return a message indicating whether the section was deleted successfully or not
	 */
	@Operation(
		summary = "Delete section by ID",
		description = "Delete section by ID, returns a message if it was deleted successfully or not.",
		parameters = {
			@Parameter(name = "id", description = "The ID of the section to be deleted")
		}
	)
	@GetMapping(value = "/delete/id", produces = "text/plain")
	public String deleteSectionById(@RequestParam("id") Integer id) {
		return sectionService.deleteSectionById(id);
	}

	/**
	 * Deletes a section by section ID.
	 *
	 * @param section the section object containing the section ID to be deleted
	 * @return a message indicating whether the section was successfully deleted or not
	 */
	@Operation(
		summary = "Delete section",
		description = "Delete section by section object, containing the section ID to be deleted and returns a message."
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteSection(@RequestBody Section section) {
		return sectionService.deleteSection(section);
	}

	/**
	 * Adds a new section to the repository.
	 *
	 * @param section the section to be added
	 * @return a string indicating the status of the operation
	 */
	@Operation(
		summary = "Add section",
		description = "Add section to the database. Returns a string if section is created or if it already exists."
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String addSection(@RequestBody Section section) {
		return sectionService.addSection(section);
	}

	/**
	 * Updates a section.
	 *
	 * @param section the section to update
	 * @return a message indicating the result of the update
	 */
	@Operation(
		summary = "Update section",
		description = "Update section in the database. Returns a string if section is not found."
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateSection(@RequestBody Section section) {
		return sectionService.updateSection(section);
	}

	// SEARCH FUNCTION
	@Operation(
		summary = "Search Section",
		description = "Search Section in the database. Returns empty list if search is empty.",
		parameters = {
			@Parameter(name = "type", description = "The type of search to be performed"),
			@Parameter(name = "search", description = "The search string")
		}
	)
	@GetMapping("/search")
	public Iterable<Section> getSectionByAdviser(@RequestParam("type") String type, @RequestParam String search) {
		if (search.isEmpty()) {
			return Collections.emptyList();
		}

		if (type.equals("teacher")) {
			return sectionService.getSectionByTeacherLastName(search);
		} else if (type.equals("section name")) {
			return sectionService.searchSectionByName(search);
		}

		return Collections.emptyList();
	}

	@Operation(
		summary = "Get Section",
		description = "Get Section by Section ID in the database.",
		parameters = {
			@Parameter(name = "sectionId", description = "The ID of the section to be retrieved")
		}
	)
	@GetMapping("/get")
	public Section getSection(@RequestParam("sectionId") Integer sectionId) {
		return sectionService.getSectionBySectionId(sectionId);
	}
}

package com.pshs.attendancesystem.controllers.section;

import com.pshs.attendancesystem.documentation.SectionDocumentation;
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
		description = SectionDocumentation.GET_ALL_SECTIONS
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
		description = SectionDocumentation.DELETE_BY_ID,
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
		description = SectionDocumentation.DELETE_SECTION
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
		summary = "Create section",
		description = SectionDocumentation.CREATE_SECTION
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String createSection(@RequestBody Section section) {
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
		description = SectionDocumentation.UPDATE_SECTION
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateSection(@RequestBody Section section) {
		return sectionService.updateSection(section);
	}

	// SEARCH FUNCTION
	@Operation(
		summary = "Search Section",
		description = SectionDocumentation.SEARCH_SECTION_BY_SECTION,
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
		description = SectionDocumentation.GET_SECTION_BY_ID,
		parameters = {
			@Parameter(name = "sectionId", description = "The ID of the section to be retrieved")
		}
	)
	@GetMapping("/get")
	public Section getSection(@RequestParam("sectionId") Integer sectionId) {
		return sectionService.getSectionBySectionId(sectionId);
	}
}

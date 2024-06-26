package com.pshs.attendancesystem.controllers.subject;

import com.pshs.attendancesystem.documentation.SubjectDocumentation;
import com.pshs.attendancesystem.entities.Subject;
import com.pshs.attendancesystem.services.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subjects", description = "The Operations related to Subjects")
@RestController
@RequestMapping("${api.root}/subject")
@SecurityRequirement(
	name = "JWT Authentication"
)
public class SubjectsController {
	private final SubjectService subjectService;

	public SubjectsController(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	/**
	 * Retrieves all subjects.
	 *
	 * @return an iterable of Subject objects representing all subjects
	 */
	@Operation(
		summary = "Retrieves all subjects",
		description = SubjectDocumentation.GET_ALL_SUBJECTS
	)
	@GetMapping("/all")
	public Iterable<Subject> getAllSubjects() {
		return subjectService.getAllSubjects();
	}

	/**
	 * Retrieves a list of subjects based on the given subject name.
	 *
	 * @param subjectName the name of the subject to search for
	 * @return an iterable collection of subjects matching the given name
	 */
	@Operation(
		summary = "Retrieves a list of subjects based on the given subject name",
		description = SubjectDocumentation.SEARCH_SUBJECTS_BY_SUBJECT_NAME
	)
	@GetMapping("/search/subject-name")
	public Iterable<Subject> searchSubjects(@RequestParam("name") String subjectName) {
		return subjectService.searchSubjectsByName(subjectName);
	}

	/**
	 * Retrieves a collection of Subject objects based on the provided subject description.
	 *
	 * @param subjectDescription the description of the subject to search for
	 * @return an Iterable collection of Subject objects
	 */
	@Operation(
		summary = "Retrieves a collection of Subject objects based on the provided subject description",
		description = SubjectDocumentation.SEARCH_SUBJECTS_BY_SUBJECT_DESCRIPTION
	)
	@GetMapping("/search/subject-description")
	public Iterable<Subject> searchSubjectsByDescription(@RequestParam("description") String subjectDescription) {
		return subjectService.searchSubjectsByDescription(subjectDescription);
	}

	/**
	 * Creates a subject using the given Subject object.
	 *
	 * @param subject the Subject object containing the details of the subject to be created
	 */
	@Operation(
		summary = "Create Subject",
		description = SubjectDocumentation.CREATE_SUBJECT
	)
	@PostMapping("/create")
	public String createSubject(@RequestBody Subject subject) {
		return subjectService.createSubject(subject);
	}

	/**
	 * Updates a subject.
	 *
	 * @param subject the subject object to be updated
	 */
	@Operation(
		summary = "Update Subject",
		description = SubjectDocumentation.UPDATE_SUBJECT
	)
	@PostMapping("/update")
	public String updateSubject(@RequestBody Subject subject) {
		return subjectService.updateSubject(subject);
	}

	/**
	 * Deletes a subject.
	 *
	 * @param subject the subject to be deleted
	 */
	@Operation(
		summary = "Delete Subject",
		description = SubjectDocumentation.DELETE_SUBJECT
	)
	@PostMapping("/delete")
	public String deleteSubject(@RequestBody Subject subject) {
		return subjectService.deleteSubject(subject);
	}

	/**
	 * Retrieves a subject by its ID.
	 *
	 * @param subjectId the ID of the subject
	 * @return the subject with the given ID
	 */
	@Operation(
		summary = "Get Subject by ID",
		description = SubjectDocumentation.GET_SUBJECT_BY_ID,
		parameters = {
			@Parameter(name = "subject-id", description = "The ID of the Subject object to be retrieved")
		}
	)
	@GetMapping("/get/subject-id")
	public Subject getSubjectById(@RequestParam("id") Integer subjectId) {
		return subjectService.getSubject(subjectId);
	}

	/**
	 * Retrieves a subject by its name.
	 *
	 * @param subjectName the name of the subject to retrieve
	 * @return the subject with the given name
	 */
	@Operation(
		summary = "Get Subject by Name",
		description = SubjectDocumentation.GET_SUBJECT_BY_NAME,
		parameters = {
			@Parameter(name = "subject-name", description = "The Name of the Subject object to be retrieved")
		}
	)
	@GetMapping("/get-by-name")
	public Subject getSubjectByName(@RequestParam("name") String subjectName) {
		return subjectService.getSubjectByName(subjectName);
	}

	/**
	 * Deletes a subject by its ID.
	 *
	 * @param id the ID of the subject to be deleted
	 */
	@Operation(
		summary = "Delete Subject by ID",
		description = SubjectDocumentation.DELETE_SUBJECT_BY_ID,
		parameters = {
			@Parameter(name = "id", description = "The ID of the Subject object to be deleted")
		}
	)
	@PostMapping("/delete/id")
	public String deleteSubjectById(@RequestParam("id") Integer id) {
		return subjectService.deleteSubjectById(id);
	}
}

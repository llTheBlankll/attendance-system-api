package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Subject;
import com.pshs.attendancesystem.services.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Subjects", description = "The Operations related to Subjects")
@RestController
@RequestMapping("/v1/subject")
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
		description = "Retrieves all subjects"
	)
	@GetMapping("/all")
	public Iterable<Subject> getAllSubjects() {
		return this.subjectService.getAllSubjects();
	}

	/**
	 * Retrieves a list of subjects based on the given subject name.
	 *
	 * @param subjectName the name of the subject to search for
	 * @return an iterable collection of subjects matching the given name
	 */
	@GetMapping("/search/subject-name/{subject-name}")
	public Iterable<Subject> searchSubjects(@PathVariable("subject-name") String subjectName) {
		return this.subjectService.searchSubjectsByName(subjectName);
	}

	/**
	 * Retrieves a collection of Subject objects based on the provided subject description.
	 *
	 * @param subjectDescription the description of the subject to search for
	 * @return an Iterable collection of Subject objects
	 */
	@Operation(
		summary = "Retrieves a collection of Subject objects based on the provided subject description",
		description = "Retrieves a collection of Subject objects based on the provided subject description",
		parameters = {
			@Parameter(name = "subject-description", description = "The description of the subject to search for")
		}
	)
	@GetMapping("/search/subject-description/{subject-description}")
	public Iterable<Subject> searchSubjectsByDescription(@PathVariable("subject-description") String subjectDescription) {
		return this.subjectService.searchSubjectsByDescription(subjectDescription);
	}

	/**
	 * Creates a subject using the given Subject object.
	 *
	 * @param subject the Subject object containing the details of the subject to be created
	 */
	@Operation(
		summary = "Create Subject",
		description = "Create Subject in the database.",
		parameters = {
			@Parameter(name = "subject", description = "The Subject object to be created")
		}
	)
	@PostMapping("/create")
	public void createSubject(@RequestBody Subject subject) {
		this.subjectService.createSubject(subject);
	}

	/**
	 * Updates a subject.
	 *
	 * @param subject the subject object to be updated
	 */
	@Operation(
		summary = "Update Subject",
		description = "Update Subject in the database.",
		parameters = {
			@Parameter(name = "subject", description = "The Subject object to be updated")
		}
	)
	@PostMapping("/update")
	public void updateSubject(@RequestBody Subject subject) {
		this.subjectService.updateSubject(subject);
	}

	/**
	 * Deletes a subject.
	 *
	 * @param subject the subject to be deleted
	 */
	@Operation(
		summary = "Delete Subject",
		description = "Delete Subject in the database.",
		parameters = {
			@Parameter(name = "subject", description = "The Subject object to be deleted")
		}
	)
	@PostMapping("/delete")
	public void deleteSubject(@RequestBody Subject subject) {
		this.subjectService.deleteSubject(subject);
	}

	/**
	 * Retrieves a subject by its ID.
	 *
	 * @param subjectId the ID of the subject
	 * @return the subject with the given ID
	 */
	@Operation(
		summary = "Get Subject by ID",
		description = "Get Subject by ID in the database.",
		parameters = {
			@Parameter(name = "subject-id", description = "The ID of the Subject object to be retrieved")
		}
	)
	@GetMapping("/get/{subject-id}")
	public Subject getSubjectById(@PathVariable("subject-id") Integer subjectId) {
		return this.subjectService.getSubject(subjectId);
	}

	/**
	 * Retrieves a subject by its name.
	 *
	 * @param subjectName the name of the subject to retrieve
	 * @return the subject with the given name
	 */
	@Operation(
		summary = "Get Subject by Name",
		description = "Get Subject by Name in the database.",
		parameters = {
			@Parameter(name = "subject-name", description = "The Name of the Subject object to be retrieved")
		}
	)
	@GetMapping("/get-by-name/{subject-name}")
	public Subject getSubjectByName(@PathVariable("subject-name") String subjectName) {
		return this.subjectService.getSubjectByName(subjectName);
	}

	/**
	 * Deletes a subject by its ID.
	 *
	 * @param id the ID of the subject to be deleted
	 */
	@Operation(
		summary = "Delete Subject by ID",
		description = "Delete Subject by ID in the database.",
		parameters = {
			@Parameter(name = "id", description = "The ID of the Subject object to be deleted")
		}
	)
	@PostMapping("/delete/{id}")
	public void deleteSubjectById(@PathVariable("id") Integer id) {
		this.subjectService.deleteSubjectById(id);
	}
}

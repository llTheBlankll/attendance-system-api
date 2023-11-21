package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Fingerprint;
import com.pshs.attendancesystem.services.FingerprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fingerprint", description = "The Fingerprint Endpoints")
@RestController
@RequestMapping("/api/v1/fingerprint")
public class FingerprintController {
	private final FingerprintService fingerprintService;

	public FingerprintController(FingerprintService fingerprintService) {
		this.fingerprintService = fingerprintService;
	}

	@Operation(
		summary = "Get All Fingerprint",
		description = "Get All Fingerprint in the database."
	)
	@GetMapping("/all")
	public Iterable<Fingerprint> getAllFingerprint() {
		return this.fingerprintService.getAllFingerprint();
	}

	@Operation(
		summary = "Create Fingerprint",
		description = "Create Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be created")
		}
	)
	@GetMapping("/create")
	public String createFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.addFingerprint(fingerprint);
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@GetMapping("/delete")
	public String deleteFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprint(fingerprint);
	}

	@Operation(
		summary = "Update Fingerprint",
		description = "Update Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be updated")
		}
	)
	@GetMapping("/update")
	public String updateFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.updateFingerprintByFingerprintId(fingerprint);
	}

	@Operation(
		summary = "Get Fingerprint",
		description = "Get Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be retrieved")
		}
	)
	@GetMapping("/get/fingerprint-id")
	public Fingerprint getFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.getFingerprintByFingerprintId(fingerprint.getFingerprintId());
	}

	@Operation(
		summary = "Get Fingerprint",
		description = "Get Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be retrieved")
		}
	)
	@GetMapping("/get/student-lrn")
	public Fingerprint getFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.getFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@GetMapping("/delete/fingerprint-id")
	public String deleteFingerprintByFingerprintId(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprintByFingerprintId(fingerprint.getFingerprintId());
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@GetMapping("/delete/student-lrn")
	public String deleteFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}
}

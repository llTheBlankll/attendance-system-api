package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Fingerprint;
import com.pshs.attendancesystem.services.FingerprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fingerprint", description = "The Fingerprint Endpoints")
@RestController
@RequestMapping("/v1/fingerprint")
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
	@PostMapping("/create")
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
	@PostMapping("/delete")
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
	@PostMapping("/update")
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
	@PostMapping("/get/fingerprint-id")
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
	@PostMapping("/get/student-lrn")
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
	@PostMapping("/delete/fingerprint-id")
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
	@PostMapping("/delete/student-lrn")
	public String deleteFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}
}

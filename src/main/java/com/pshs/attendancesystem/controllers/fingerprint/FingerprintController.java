package com.pshs.attendancesystem.controllers.fingerprint;

import com.pshs.attendancesystem.entities.Fingerprint;
import com.pshs.attendancesystem.services.FingerprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fingerprint", description = "The Fingerprint Endpoints")
@RestController
@RequestMapping("${api.root}/fingerprint")
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
		return fingerprintService.getAllFingerprint();
	}

	@Operation(
		summary = "Create Fingerprint",
		description = "Create Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be created")
		}
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String createFingerprint(@RequestBody Fingerprint fingerprint) {
		return fingerprintService.addFingerprint(fingerprint);
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteFingerprint(@RequestBody Fingerprint fingerprint) {
		return fingerprintService.deleteFingerprint(fingerprint);
	}

	@Operation(
		summary = "Update Fingerprint",
		description = "Update Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be updated")
		}
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateFingerprint(@RequestBody Fingerprint fingerprint) {
		return fingerprintService.updateFingerprintByFingerprintId(fingerprint);
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
		return fingerprintService.getFingerprintByFingerprintId(fingerprint.getFingerprintId());
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
		return fingerprintService.getFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@PostMapping(value = "/delete/fingerprint-id", produces = "text/plain")
	public String deleteFingerprintByFingerprintId(@RequestBody Fingerprint fingerprint) {
		return fingerprintService.deleteFingerprintByFingerprintId(fingerprint.getFingerprintId());
	}

	@Operation(
		summary = "Delete Fingerprint",
		description = "Delete Fingerprint in the database.",
		parameters = {
			@Parameter(name = "fingerprint", description = "The Fingerprint object to be deleted")
		}
	)
	@PostMapping(value = "/delete/student-lrn", produces = "text/plain")
	public String deleteFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return fingerprintService.deleteFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}
}

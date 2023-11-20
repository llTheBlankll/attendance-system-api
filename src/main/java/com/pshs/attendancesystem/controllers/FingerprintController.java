package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Fingerprint;
import com.pshs.attendancesystem.services.FingerprintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fingerprint")
public class FingerprintController {
	private final FingerprintService fingerprintService;

	public FingerprintController(FingerprintService fingerprintService) {
		this.fingerprintService = fingerprintService;
	}

	@GetMapping("/all")
	public Iterable<Fingerprint> getAllFingerprint() {
		return this.fingerprintService.getAllFingerprint();
	}

	@GetMapping("/create")
	public String createFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.addFingerprint(fingerprint);
	}

	@GetMapping("/delete")
	public String deleteFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprint(fingerprint);
	}

	@GetMapping("/update")
	public String updateFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.updateFingerprintByFingerprintId(fingerprint);
	}

	@GetMapping("/get/fingerprint-id")
	public Fingerprint getFingerprint(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.getFingerprintByFingerprintId(fingerprint.getFingerprintId());
	}

	@GetMapping("/get/student-lrn")
	public Fingerprint getFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.getFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}

	@GetMapping("/delete/fingerprint-id")
	public String deleteFingerprintByFingerprintId(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprintByFingerprintId(fingerprint.getFingerprintId());
	}

	@GetMapping("/delete/student-lrn")
	public String deleteFingerprintByStudentLrn(@RequestBody Fingerprint fingerprint) {
		return this.fingerprintService.deleteFingerprintByStudentLrn(fingerprint.getStudent().getLrn());
	}
}

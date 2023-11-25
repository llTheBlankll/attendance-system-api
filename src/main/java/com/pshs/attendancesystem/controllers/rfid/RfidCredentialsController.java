package com.pshs.attendancesystem.controllers.rfid;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.services.RfidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "RFID Credentials", description = "The RFID Controller Endpoints")
@RestController
@RequestMapping("/v1/rfid")
@PreAuthorize("hasRole('RFID_DEVICE')")
public class RfidCredentialsController {

	private final RfidService rfidService;

	public RfidCredentialsController(RfidService rfidService) {
		this.rfidService = rfidService;
	}

	/**
	 * Retrieves all the scans.
	 *
	 * @return an iterable collection of Scan objects representing all the scans
	 */
	@Operation(
		summary = "Retrieves all the rfid credentials",
		description = "Retrieves all the rfid credentials. " +
			"Returns an empty list if there are no rfid credentials in the database."
	)
	@GetMapping("/all")
	public Iterable<RfidCredentials> getAllScan() {
		return rfidService.getAllRfidCredentials();
	}

	/**
	 * Retrieves an RfidCredentials object based on the provided type and data.
	 *
	 * @param type the type of data to search for (hash or studentLrn)
	 * @param data the data to search for (hashed value or studentLrn)
	 * @return the retrieved RfidCredentials object
	 */
	@Operation(
		summary = "Retrieves an RfidCredentials object based on the provided type and data",
		description = "Retrieves an RfidCredentials object based on the provided type and data. " +
			"If it is hash, you should provide appropriate hashed value. " +
			"If it is studentLrn, you should provide appropriate student lrn. ",
		parameters = {
			@Parameter(name = "type", description = "The type of data to search for (hash or student lrn)"),
			@Parameter(name = "data", description = "The data to search for (hashed value or student lrn)")
		}
	)
	@GetMapping("/get")
	public Optional<RfidCredentials> getStudent(@RequestParam String type, @RequestParam String data) {
		if (type.equals("hash")) {
			return rfidService.getRfidCredentialByHashedLrn(data);
		} else {
			return rfidService.getRfidCredentialByStudentLrn(Long.parseLong(data));
		}
	}
}

package com.pshs.attendancesystem.controllers.rfid;

import com.pshs.attendancesystem.documentation.RFIDDocumentation;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.RfidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "RFID Credentials", description = "The RFID Controller Endpoints")
@RestController
@RequestMapping("${api.root}/rfid")
@SecurityRequirement(
	name = "JWT Authentication"
)
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
		description = RFIDDocumentation.GET_ALL_RFID
	)
	@GetMapping("/all")
	public Iterable<RfidCredentials> getAllCredentials() {
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
		description = RFIDDocumentation.SEARCH_RFID_BY_TYPE,
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

	@PostMapping("/toggle/student")
	public boolean toggleRfidStatus(@RequestBody Student student) {
		return rfidService.toggleRfidStatus(student.getLrn());
	}

	@GetMapping("/toggle/hash")
	public boolean toggleRfidStatus(@RequestParam String hash) {
		return rfidService.toggleRfidStatus(hash);
	}

	@GetMapping("/toggle/lrn")
	public boolean toggleRfidStatus(@RequestParam Long lrn) {
		return rfidService.toggleRfidStatus(lrn);
	}

	@PostMapping("/toggle")
	public boolean toggleRfidStatus(@RequestBody RfidCredentials credentials) {
		return rfidService.toggleRfidStatus(credentials.getHashedLrn());
	}
}

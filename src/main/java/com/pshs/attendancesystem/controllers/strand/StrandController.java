package com.pshs.attendancesystem.controllers.strand;

import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.services.StrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Strand", description = "The Strand Endpoints")
@RestController
@RequestMapping("${api.root}/strand")
public class StrandController {

	private final StrandService strandService;

	public StrandController(StrandService strandService) {
		this.strandService = strandService;
	}

	@Operation(
		summary = "Get All Strand",
		description = "Get All Strand."
	)
	@GetMapping("/all")
	public Iterable<Strand> getAllStrand() {
		return strandService.getAllStrand();
	}

	@Operation(
		summary = "Create Strand",
		description = "Create Strand in the database. Returns a message if it successful or already exists."
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String createStrand(@RequestBody Strand strand) {
		return strandService.createStrand(strand);
	}

	@Operation(
		summary = "Update Strand",
		description = "Update Strand in the database. Returns a message if it successful or not."
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateStrand(@RequestBody Strand strand) {
		return strandService.updateStrand(strand);
	}

	@Operation(
		summary = "Delete Strand",
		description = "Delete Strand in the database. Returns a message if it successful or not."
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteStrand(@RequestBody Strand strand) {
		return strandService.deleteStrand(strand);
	}

	@Operation(
		summary = "Search Strand",
		description = "Search Strand in the database. Returns empty list if search is empty.",
		parameters = {
			@Parameter(name = "strand-name", description = "The Strand Name of the Strand object to be retrieved")
		}
	)
	@GetMapping("/search/strand-name")
	public Iterable<Strand> searchStrandByStrandName(@RequestParam("name") String name) {
		return strandService.searchStrandByName(name);
	}
}

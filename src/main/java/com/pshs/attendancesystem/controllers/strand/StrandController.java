package com.pshs.attendancesystem.controllers.strand;

import com.pshs.attendancesystem.documentation.StrandDocumentation;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.services.StrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Strand", description = "The Strand Endpoints")
@RestController
@RequestMapping("${api.root}/strand")
@SecurityRequirement(
	name = "JWT Authentication"
)
public class StrandController {

	private final StrandService strandService;

	public StrandController(StrandService strandService) {
		this.strandService = strandService;
	}

	@Operation(
		summary = "Get All Strand",
		description = StrandDocumentation.GET_ALL_STRAND
	)
	@GetMapping("/all")
	public Iterable<Strand> getAllStrand() {
		return strandService.getAllStrand();
	}

	@Operation(
		summary = "Create Strand",
		description = StrandDocumentation.CREATE_STRAND
	)
	@PostMapping(value = "/create", produces = "text/plain")
	public String createStrand(@RequestBody Strand strand) {
		return strandService.createStrand(strand);
	}

	@Operation(
		summary = "Update Strand",
		description = StrandDocumentation.UPDATE_STRAND
	)
	@PostMapping(value = "/update", produces = "text/plain")
	public String updateStrand(@RequestBody Strand strand) {
		return strandService.updateStrand(strand);
	}

	@Operation(
		summary = "Delete Strand",
		description = StrandDocumentation.DELETE_STRAND
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public String deleteStrand(@RequestBody Strand strand) {
		return strandService.deleteStrand(strand);
	}

	@Operation(
		summary = "Search Strand",
		description = StrandDocumentation.SEARCH_STRAND_BY_STRAND_NAME,
		parameters = {
			@Parameter(name = "strand-name", description = "The Strand Name of the Strand object to be retrieved")
		}
	)
	@GetMapping("/search/strand-name")
	public Iterable<Strand> searchStrandByStrandName(@RequestParam("q") String name) {
		return strandService.searchStrandByName(name);
	}
}

package com.pshs.attendancesystem.controllers.role;

import com.pshs.attendancesystem.documentation.RoleDocumentation;
import com.pshs.attendancesystem.entities.Role;
import com.pshs.attendancesystem.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "JWT Authentication")
@Tag(name = "Role", description = "Role API")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@Operation(
		summary = "Get all roles",
		description = RoleDocumentation.GET_ALL_ROLES
	)
	@GetMapping("/roles")
	public Iterable<Role> getAllRoles() {
		return roleService.getAllRoles();
	}

	@Operation(
		summary = "Search roles by name",
		description = RoleDocumentation.SEARCH_ROLES,
		parameters = @Parameter(name = "q", description = "Search role by name.")
	)
	@GetMapping("/search")
	public List<Role> searchRoles(@RequestParam String q) {
		return roleService.searchRoleByName(q);
	}

	@Operation(
		summary = "Create a role",
		description = RoleDocumentation.CREATE_ROLE
	)
	@PostMapping("/create")
	public String createRole(@RequestBody Role role) {
		return roleService.createRole(role);
	}

	@Operation(
		summary = "Delete a role",
		description = RoleDocumentation.DELETE_ROLE
	)
	@PostMapping("/delete")
	public String deleteRole(@RequestBody Role role) {
		return roleService.deleteRole(role);
	}

	@Operation(
		summary = "Update a role",
		description = RoleDocumentation.UPDATE_ROLE
	)
	@PostMapping("/update")
	public String updateRole(@RequestBody Role role) {
		return roleService.updateRole(role);
	}

	@Operation(
		summary = "Get a role by id",
		description = RoleDocumentation.GET_ROLE_BY_ID
	)
	@GetMapping("/role/{id}")
	public Role getRoleById(@PathVariable Integer id) {
		return roleService.getRoleById(id);
	}

	@Operation(
		summary = "Check if a role exist by id",
		description = RoleDocumentation.IS_ROLE_EXIST_BY_ID
	)
	@GetMapping("/role/exist/id")
	public boolean isRoleExistById(@RequestParam Integer id) {
		return roleService.isRoleExistById(id);
	}

	@Operation(
		summary = "Check if a role exist",
		description = RoleDocumentation.IS_ROLE_EXIST_BY_ID
	)
	@GetMapping("/role/exist")
	public boolean isRoleExist(@RequestBody Role role) {
		return roleService.isRoleExist(role);
	}
}
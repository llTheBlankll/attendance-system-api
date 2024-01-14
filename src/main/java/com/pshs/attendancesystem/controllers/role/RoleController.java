package com.pshs.attendancesystem.controllers.role;

import com.pshs.attendancesystem.entities.Role;
import com.pshs.attendancesystem.services.RoleService;
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

	@GetMapping("/roles")
	public List<Role> getAllRoles() {
		return roleService.getAllRoles();
	}

	@GetMapping("/search")
	public List<Role> searchRoles(@RequestParam String q) {
		return roleService.searchRoleByName(q);
	}

	@PostMapping("/create")
	public String createRole(@RequestBody Role role) {
		return roleService.createRole(role);
	}

	@PostMapping("/delete")
	public String deleteRole(@RequestBody Role role) {
		return roleService.deleteRole(role);
	}

	@PostMapping("/update")
	public String updateRole(@RequestBody Role role) {
		return roleService.updateRole(role);
	}

	@GetMapping("/role/{id}")
	public Role getRoleById(@PathVariable Integer id) {
		return roleService.getRoleById(id);
	}

	@GetMapping("/role/exist/id")
	public boolean isRoleExistById(@RequestParam Integer id) {
		return roleService.isRoleExistById(id);
	}

	@GetMapping("/role/exist")
	public boolean isRoleExist(@RequestBody Role role) {
		return roleService.isRoleExist(role);
	}
}
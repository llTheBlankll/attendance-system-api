package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Role;
import com.pshs.attendancesystem.messages.RoleMessages;
import com.pshs.attendancesystem.repositories.RoleRepository;
import com.pshs.attendancesystem.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Iterable<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public String createRole(Role role) {
		if (isRoleExist(role)) {
			return RoleMessages.ROLE_ALREADY_EXIST;
		}

		roleRepository.save(role);
		return RoleMessages.ROLE_CREATED_SUCCESSFULLY;
	}

	@Override
	public String deleteRole(Role role) {
		if (isRoleExist(role)) {
			roleRepository.delete(role);
			return RoleMessages.ROLE_DELETED_SUCCESSFULLY;
		}

		return RoleMessages.ROLE_NOT_FOUND;
	}

	@Override
	public String updateRole(Role role) {
		if (isRoleExist(role)) {
			roleRepository.save(role);
			return RoleMessages.ROLE_UPDATED_SUCCESSFULLY;
		}

		return RoleMessages.ROLE_NOT_FOUND;
	}

	@Override
	public Role getRoleById(Integer id) {
		return roleRepository.findById(id).orElse(null);
	}

	@Override
	public List<Role> searchRoleByName(String roleName) {
		return roleRepository.searchByName(roleName);
	}

	@Override
	public boolean isRoleExist(Role role) {
		return roleRepository.existsById(role.getId());
	}

	@Override
	public boolean isRoleExistById(Integer id) {
		return roleRepository.existsById(id);
	}
}

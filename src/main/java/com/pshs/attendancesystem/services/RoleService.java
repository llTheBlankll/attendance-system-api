package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    String createRole(Role role);

    String deleteRole(Role role);

    String updateRole(Role role);

    Role getRoleById(Integer id);

    List<Role> searchRoleByName(String roleName);

    boolean isRoleExist(Role role);
    boolean isRoleExistById(Integer id);

}

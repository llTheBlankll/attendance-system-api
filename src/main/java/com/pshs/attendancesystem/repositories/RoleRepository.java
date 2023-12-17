package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
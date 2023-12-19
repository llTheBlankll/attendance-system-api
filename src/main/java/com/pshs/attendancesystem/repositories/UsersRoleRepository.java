package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.UsersRole;
import org.springframework.data.repository.CrudRepository;

public interface UsersRoleRepository extends CrudRepository<UsersRole, Integer> {
}
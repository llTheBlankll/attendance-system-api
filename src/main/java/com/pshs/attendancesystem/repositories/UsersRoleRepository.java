package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.UsersRole;
import com.pshs.attendancesystem.entities.UsersRoleId;
import org.springframework.data.repository.CrudRepository;

public interface UsersRoleRepository extends CrudRepository<UsersRole, UsersRoleId> {
}
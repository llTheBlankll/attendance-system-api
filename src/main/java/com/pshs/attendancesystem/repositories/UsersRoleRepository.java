package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.UsersRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRoleRepository extends CrudRepository<UsersRole, Integer> {
}
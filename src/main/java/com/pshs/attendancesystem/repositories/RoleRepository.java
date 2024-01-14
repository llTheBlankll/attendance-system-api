package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
	@Query("select r from Role r where r.roleName like concat('%', ?1, '%')")
	List<Role> searchByName(String roleName);
}
package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	@Query("select (count(u) > 0) from User u where u.userName = ?1")
	boolean isUsernameExist(String userName);
}
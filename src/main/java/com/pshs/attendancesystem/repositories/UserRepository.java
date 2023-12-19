package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	@Query("select (count(u) > 0) from User u where u.userName = ?1")
	boolean isUsernameExist(String userName);

	@Transactional
	@Modifying
	@Query("update User u set u.lastLogin = ?1 where u.userName = ?2")
	int updateUserLastLogin(LocalDateTime lastLogin, String userName);

	@Query("select u from User u where u.userName = ?1")
	List<User> findUserByUsername(String userName);

	@Query("select u from User u where upper(u.userName) like upper(concat('%', ?1, '%'))")
	List<User> findUsersByUsername(String userName);

	@Query("select u from User u where upper(u.email) like upper(concat('%', ?1, '%'))")
	List<User> findUsersByEmail(String email);
}
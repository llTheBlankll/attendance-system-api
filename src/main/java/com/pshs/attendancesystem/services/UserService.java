package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.User;

import java.util.List;

public interface UserService {

	void updateUserLastLogin(String username);

	Iterable<User> getAllUsers();

	List<User> searchByUsername(String username);

	List<User> searchByEmail(String email);
}

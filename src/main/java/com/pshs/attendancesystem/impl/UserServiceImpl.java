package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.repositories.UserRepository;
import com.pshs.attendancesystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final Logger logger = LogManager.getLogger(this.getClass());

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@CachePut(value = "user", key = "#username")
	public void updateUserLastLogin(String username) {
		if (userRepository.updateUserLastLogin(LocalDateTime.now(), username) <= 0) {
			logger.info("User not found");
		} else {
			logger.debug("User last login updated");
		}
	}

	@Override
	@Cacheable(value = "user", key = "#username")
	public List<User> searchByUsername(String username) {
		return userRepository.findUsersByUsername(username);
	}

	@Override
	@Cacheable(value = "user", key = "#email")
	public List<User> searchByEmail(String email) {
		return userRepository.findUsersByEmail(email);
	}

	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}

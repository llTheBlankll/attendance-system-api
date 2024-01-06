package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.repositories.UserRepository;
import com.pshs.attendancesystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig(cacheNames = {
	"user",
})
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final Logger logger = LogManager.getLogger(this.getClass());


	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	@Caching(
		evict = @CacheEvict(key = "#username"),
		put = @CachePut(key = "#username")
	)
	public void updateUserLastLogin(String username) {
		LocalDateTime now = LocalDateTime.now();
		if (userRepository.updateUserLastLogin(now, username) <= 0) {
			logger.info("User" + username + " not found");
		} else {
			logger.debug("User " + username + " last login updated: " + now);
		}
	}

	@Override
	@Cacheable(key = "#username")
	public List<User> searchByUsername(String username) {
		return userRepository.findUsersByUsername(username);
	}

	@Override
	@Cacheable(key = "#email")
	public List<User> searchByEmail(String email) {
		return userRepository.findUsersByEmail(email);
	}

	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}

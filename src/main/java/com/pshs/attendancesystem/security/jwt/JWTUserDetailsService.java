package com.pshs.attendancesystem.security.jwt;

import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JWTUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public JWTUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}
}

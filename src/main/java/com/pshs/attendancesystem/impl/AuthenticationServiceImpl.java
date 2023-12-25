package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.repositories.UserRepository;
import com.pshs.attendancesystem.services.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final Logger logger = LogManager.getLogger(this.getClass());

	public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public User signUp(RegisterUserDTO registerDTO) {
		User user = new User();
		user.setUsername(registerDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setEmail(registerDTO.getEmail());
		user.setRoles(registerDTO.getRoles());

		if (userRepository.isUsernameExist(registerDTO.getUsername())) {
			logger.debug("Username {} already exist", registerDTO.getUsername());
			return null;
		} else {
			// Save the user first, before saving the users' role.
			return userRepository.save(user);
		}
	}

	@Override
	public User signIn(LoginUserDTO loginDTO) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(),
				loginDTO.getPassword()
			)
		);

		return userRepository.findByUserName(loginDTO.getUsername()).orElseThrow(
			() -> new UsernameNotFoundException("User not found")
		);
	}
}

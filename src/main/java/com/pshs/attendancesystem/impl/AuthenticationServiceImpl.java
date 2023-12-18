package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.repositories.UserRepository;
import com.pshs.attendancesystem.services.AuthenticationService;
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

	public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public User signUp(RegisterUserDTO registerDTO) {
		User user = new User();
		user.setUserName(registerDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		user.setEmail(registerDTO.getEmail());
		user.setRole(registerDTO.getRole());

		if (userRepository.isUsernameExist(registerDTO.getUsername())) {
			return null;
		} else {
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

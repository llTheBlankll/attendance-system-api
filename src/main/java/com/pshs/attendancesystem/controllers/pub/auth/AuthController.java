package com.pshs.attendancesystem.controllers.pub.auth;

import com.pshs.attendancesystem.dto.LoginResponse;
import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.security.jwt.JwtService;
import com.pshs.attendancesystem.services.AuthenticationService;
import com.pshs.attendancesystem.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	private final UserService userService;

	public AuthController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public User login(@RequestBody RegisterUserDTO registerUserDTO) {
		return authenticationService.signUp(registerUserDTO);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginUserDTO loginUserDTO) {
		User authenticatedUser = authenticationService.signIn(loginUserDTO);

		LoginResponse loginResponse = new LoginResponse(
			jwtService.generateToken(authenticatedUser)
		);

		// Update User Last Log in
		userService.updateUserLastLogin(authenticatedUser.getUsername());

		return loginResponse;
	}

	@GetMapping(value = "/ping")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PRINCIPAL')")
	public String ping() {
		return "pong";
	}
}

package com.pshs.attendancesystem.controllers.auth;

import com.pshs.attendancesystem.dto.LoginResponse;
import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.security.jwt.JwtService;
import com.pshs.attendancesystem.services.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.root}/auth")
public class AuthController {

	private final JwtService jwtService;
	private final AuthenticationService authenticationService;

	public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/register")
	public User login(@RequestBody RegisterUserDTO registerUserDTO) {
		return authenticationService.signUp(registerUserDTO);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginUserDTO loginUserDTO) {
		User authenticatedUser = authenticationService.signIn(loginUserDTO);

		return new LoginResponse(
			jwtService.generateToken(authenticatedUser)
		);
	}

	@GetMapping("/ping")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PRINCIPAL')")
	public String ping() {
		return "pong";
	}
}

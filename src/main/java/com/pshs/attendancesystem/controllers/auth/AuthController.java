package com.pshs.attendancesystem.controllers.auth;

import com.pshs.attendancesystem.dto.ErrorDTO;
import com.pshs.attendancesystem.dto.LoginResponse;
import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.security.jwt.JwtService;
import com.pshs.attendancesystem.services.AuthenticationService;
import com.pshs.attendancesystem.services.UserService;
import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
@SecurityRequirement(
	name = "JWT Authentication"
)
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
	public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO) {
		try {
			User authenticatedUser = authenticationService.signIn(loginUserDTO);

			LoginResponse loginResponse = new LoginResponse(
				jwtService.generateToken(authenticatedUser)
			);

			// Update User Last Log in
			userService.updateUserLastLogin(authenticatedUser.getUsername());

			return ResponseEntity.ok(loginResponse);
		} catch (BadCredentialsException badCredentialsException) {
			Sentry.captureMessage(badCredentialsException.getMessage());
			ErrorDTO errorDTO = new ErrorDTO(400, badCredentialsException.getMessage());
			return ResponseEntity.badRequest().body(errorDTO);
		}
	}

	@GetMapping(value = "/ping")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PRINCIPAL')")
	public String ping() {
		return "pong";
	}
}

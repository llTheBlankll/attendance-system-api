package com.pshs.attendancesystem.controllers.auth;

import com.pshs.attendancesystem.dto.LoginResponse;
import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.security.jwt.JwtService;
import com.pshs.attendancesystem.services.AuthenticationService;
import com.pshs.attendancesystem.services.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private final JwtService jwtService;
	private final UserService userService;
	private final AuthenticationService authenticationService;

	public LoginController(JwtService jwtService, UserService userService, AuthenticationService authenticationService) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/auth/login/form")
	public String loginForm(Model model, @RequestParam(required = false) String message) {
		model.addAttribute("loginDTO", new LoginUserDTO());
		model.addAttribute("message", (message != null) ? message : false);

		return "login";
	}

	@PostMapping("/auth/login/api")
	public String loginApi(@ModelAttribute LoginUserDTO loginUserDTO, Model model) {
		try {
			System.out.println("User: " + loginUserDTO.getUsername());
			// Sign in
			User authenticatedUser = authenticationService.signIn(loginUserDTO);

			LoginResponse loginResponse = new LoginResponse(
				jwtService.generateToken(authenticatedUser)
			);

			// Update User Last Log in
			userService.updateUserLastLogin(authenticatedUser.getUsername());
			model.addAttribute("loginResponse", loginResponse);

			return "token";
		} catch (BadCredentialsException e) {
			return "redirect:/auth/login/form?message=Invalid username or password";
		}
	}
}

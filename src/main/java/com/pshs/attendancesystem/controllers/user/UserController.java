package com.pshs.attendancesystem.controllers.user;

import com.pshs.attendancesystem.entities.User;
import com.pshs.attendancesystem.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.root}/user")
@Tag(name = "User")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/all")
	public Iterable<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/search/email")
	public List<User> searchUserByEmail(@RequestParam("q") String email) {
		return userService.searchByEmail(email);
	}

	@GetMapping("/search/username")
	public List<User> searchUserByUsername(@RequestParam("q") String username) {
		return userService.searchByUsername(username);
	}
}

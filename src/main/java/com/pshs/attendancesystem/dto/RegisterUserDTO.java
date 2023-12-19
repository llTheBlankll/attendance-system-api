package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.entities.Role;

import java.util.Set;

public class RegisterUserDTO {

	private String username;
	private String password;
	private String email;
	private Set<Role> role;

	public RegisterUserDTO(String username, String password, String email, Set<Role> role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}
}

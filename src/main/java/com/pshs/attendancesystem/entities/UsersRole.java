package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "users_role")
public class UsersRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "users_role_id", nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
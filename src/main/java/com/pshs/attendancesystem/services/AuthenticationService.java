package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.dto.LoginUserDTO;
import com.pshs.attendancesystem.dto.RegisterUserDTO;
import com.pshs.attendancesystem.entities.User;

public interface AuthenticationService {

	User signUp(RegisterUserDTO registerDTO);

	User signIn(LoginUserDTO loginDTO);
}

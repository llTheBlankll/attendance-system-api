package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
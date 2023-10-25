package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.RfidCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RfidCredentialsRepository extends JpaRepository<RfidCredentials, Long> {
    RfidCredentials findByHashedLrn(String hashedLrn);

    boolean existsByHashedLrn(String hashedLrn);
}
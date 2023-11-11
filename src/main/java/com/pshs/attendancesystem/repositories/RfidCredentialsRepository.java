package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.RfidCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RfidCredentialsRepository extends JpaRepository<RfidCredentials, Long> {
    Optional<RfidCredentials> findByHashedLrn(String hashedLrn);
    Optional<RfidCredentials> findByLrn(Long lrn);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean existsByHashedLrn(String hashedLrn);
}
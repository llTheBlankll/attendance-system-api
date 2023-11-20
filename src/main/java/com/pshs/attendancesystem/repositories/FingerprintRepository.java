package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Integer> {
    Optional<Fingerprint> findByFingerprintId(String fingerprintId);
}
package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanRepository extends JpaRepository<Scan, Long> {
    Scan findByHashedLrn(String hashedLrn);
}
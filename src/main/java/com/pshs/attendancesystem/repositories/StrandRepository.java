package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Strand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrandRepository extends JpaRepository<Strand, Integer> {
}
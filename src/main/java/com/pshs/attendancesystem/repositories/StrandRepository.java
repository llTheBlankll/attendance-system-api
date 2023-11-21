package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Strand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface StrandRepository extends JpaRepository<Strand, Integer> {
	Iterable<Strand> findByStrandNameContainsIgnoreCase(String strandName);

	boolean existsByStrandName(@NonNull String strandName);
}
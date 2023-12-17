package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Integer> {
	@Query("select f from Fingerprint f where f.fingerprintId = ?1")
	Optional<Fingerprint> getFingerprintId(String fingerprintId);

	@Query("select f from Fingerprint f where f.student.lrn = ?1")
	Optional<Fingerprint> getStudentLrn(Long studentLrn);
}
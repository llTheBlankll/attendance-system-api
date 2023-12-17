package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.RfidCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RfidCredentialsRepository extends JpaRepository<RfidCredentials, Long> {
	Optional<RfidCredentials> findByHashedLrn(String hashedLrn);

	Optional<RfidCredentials> findByLrn(Long lrn);

	@Query("select (count(r) > 0) from RfidCredentials r where r.hashedLrn = ?1")
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean isHashedLrnExist(String hashedLrn);

	@Query("select (count(r) > 0) from RfidCredentials r where r.lrn = ?1")
	boolean isLrnExist(Long lrn);
}
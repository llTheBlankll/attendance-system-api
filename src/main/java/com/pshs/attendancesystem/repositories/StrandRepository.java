package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Strand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface StrandRepository extends JpaRepository<Strand, Integer> {
	@Query("select s from Strand s where upper(s.strandName) like upper(concat('%', ?1, '%'))")
	Iterable<Strand> searchStrandName(String strandName);

	@Query("select (count(s) > 0) from Strand s where s.strandName = ?1")
	boolean isStrandNameExist(@NonNull String strandName);
}
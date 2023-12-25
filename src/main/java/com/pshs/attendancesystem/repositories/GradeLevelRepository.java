package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Gradelevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeLevelRepository extends JpaRepository<Gradelevel, Integer> {

	@Query("select g from Gradelevel g where upper(g.gradeName) like upper(concat('%', ?1, '%'))")
	Iterable<Gradelevel> searchGradeLevelByName(String gradeName);

	@Query("select g from Gradelevel g where g.gradeName = ?1")
	Optional<Gradelevel> getAllStudentByGradeName(String gradeName);
}
package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Gradelevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeLevelRepository extends JpaRepository<Gradelevel, Integer> {

	@Query("select g from Gradelevel g where upper(g.gradeName) like upper(concat('%', ?1, '%'))")
	Iterable<Gradelevel> searchGradeLevelByName(String gradeName);
}
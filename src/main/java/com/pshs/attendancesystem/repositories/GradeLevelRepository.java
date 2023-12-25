package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GradeLevelRepository extends JpaRepository<Gradelevel, Integer> {

	@Query("select g from Gradelevel g where upper(g.gradeName) like upper(concat('%', ?1, '%'))")
	Iterable<Gradelevel> searchGradeLevelByName(String gradeName);

	@Query("select g from Gradelevel g where g.gradeName = ?1")
	Optional<Gradelevel> getAllStudentByGradeName(String gradeName);

	@Transactional
	@Modifying
	@Query("update Gradelevel g set g.strand = ?1 where g.id = ?2")
	int updateStrandById(@Nullable Strand strand, Integer id);
}
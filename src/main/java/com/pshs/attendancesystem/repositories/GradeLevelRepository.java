package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Gradelevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeLevelRepository extends JpaRepository<Gradelevel, Integer> {

}
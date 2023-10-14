package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, String> {
    boolean existsByAdviser(String adviser);
    Iterable<Section> findByAdviserLikeIgnoreCase(String adviser);
}
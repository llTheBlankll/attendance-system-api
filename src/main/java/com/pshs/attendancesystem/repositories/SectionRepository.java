package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, String> {
}
package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
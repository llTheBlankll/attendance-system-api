package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
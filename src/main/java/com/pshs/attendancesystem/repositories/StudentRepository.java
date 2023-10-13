package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentById(Long studentLrn);

    boolean existsById(Long id);
    Iterable<Student> findStudentsByStudentGradeLevel_GradeName(String gradeName);
    boolean existsByStudentGradeLevel_GradeName(String gradeName);
}
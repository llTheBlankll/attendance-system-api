package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByLrn(Long studentLrn);

    boolean existsById(Long id);
    Iterable<Student> findStudentsByStudentGradeLevel_GradeName(String gradeName);
    boolean existsByStudentGradeLevel_GradeName(String gradeName);

    Iterable<Student> findStudentsByStudentSection_SectionId(String sectionId);

    long countStudentsByStudentSectionSectionId(String sectionId);
}
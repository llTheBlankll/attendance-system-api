package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRepository extends JpaRepository<Guardian, Integer> {
    Iterable<Guardian> findByStudent(Student student);

    Iterable<Guardian> findByStudentLrn(Long studentLrn);

    Iterable<Guardian> findGuardiansByFullNameIgnoreCase(String fullName);

    boolean existsByStudentLrn(long studentLrn);
}
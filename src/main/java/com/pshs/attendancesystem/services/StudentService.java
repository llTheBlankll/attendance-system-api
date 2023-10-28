package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Student;

public interface StudentService {
    Student findStudentByLrn(Long studentLrn);

    boolean existsById(Long id);

    Iterable<Student> findStudentsByStudentGradeLevel_GradeName(String gradeName);

    boolean existsByStudentGradeLevel_GradeName(String gradeName);

}

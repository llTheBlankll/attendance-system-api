package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Override
    public Student findStudentByLrn(Long studentLrn) {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public Iterable<Student> findStudentsByStudentGradeLevel_GradeName(String gradeName) {
        return null;
    }

    @Override
    public boolean existsByStudentGradeLevel_GradeName(String gradeName) {
        return false;
    }
}

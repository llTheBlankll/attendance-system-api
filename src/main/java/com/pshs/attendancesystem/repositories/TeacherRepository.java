package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Iterable<Teacher> findTeachersByLastNameIgnoreCase(String lastName);

    Iterable<Teacher> findTeachersBySubjectExpertise_Name(String subjectExpertiseName);
}
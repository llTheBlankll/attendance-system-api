package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;

public interface GuardianService {
    long getGuardiansCount();

    Iterable<Guardian> getStudentGuardians(Student student);

    Iterable<Guardian> getStudentGuardiansByLrn(Long lrn);

    Iterable<Guardian> getGuardianByLastName(String lastName);
}
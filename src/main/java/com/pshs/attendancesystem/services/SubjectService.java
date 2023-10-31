package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Subject;

public interface SubjectService {

    void deleteSubject(Integer subject_id);

    Integer createSubject(Subject subject);

    Subject getSubject(Integer subject_id);

    Iterable<Subject> getSubjectByName(String subjectName);

    Iterable<Subject> searchSubjectBytName(String subjectName);
}

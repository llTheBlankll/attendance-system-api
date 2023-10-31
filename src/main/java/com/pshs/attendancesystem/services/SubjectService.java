package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Subject;

public interface SubjectService {

    void deleteSubject(Integer subjectId);

    Integer createSubject(Subject subject);

    Subject getSubject(Integer subjectId);

    Iterable<Subject> searchSubjectsByName(String subjectName);

    Subject getSubjectByName(String subjectName);

    Iterable<Subject> searchSubjectsByDescription(String subjectDescription);

    void deleteSubject(Subject subject);

    Subject updateSubject(Subject subject);

    void deleteSubjectById(Integer subjectId);
}

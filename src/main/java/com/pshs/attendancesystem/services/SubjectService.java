package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Subject;

public interface SubjectService {

    void deleteSubject(Subject subjectId);

    void createSubject(Subject subject);

    Subject getSubject(Integer subjectId);

    Iterable<Subject> searchSubjectsByName(String subjectName);

    Subject getSubjectByName(String subjectName);

    Iterable<Subject> searchSubjectsByDescription(String subjectDescription);


    void updateSubject(Subject subject);

    void deleteSubjectById(Integer subjectId);

    Iterable<Subject> getAllSubjects();
}

package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Subject;

public interface SubjectService {

	String deleteSubject(Subject subjectId);

	String createSubject(Subject subject);

	Subject getSubject(Integer subjectId);

	Iterable<Subject> searchSubjectsByName(String subjectName);

	Subject getSubjectByName(String subjectName);

	Iterable<Subject> searchSubjectsByDescription(String subjectDescription);

	String updateSubject(Subject subject);

	String deleteSubjectById(Integer subjectId);

	Iterable<Subject> getAllSubjects();
}

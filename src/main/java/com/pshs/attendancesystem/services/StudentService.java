package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Student;

import java.time.LocalDate;

public interface StudentService {
	String createStudent(Student student);

	String deleteStudent(Student student);

	String deleteStudentById(Long id);

	Iterable<Student> getStudentByGradeLevel(String gradeLevel);

	boolean isStudentAttended(Long lrn, LocalDate date);

	Student getStudentById(Long lrn);

	String updateStudent(Student student);

	Iterable<Student> getAllStudentWithSectionId(Integer sectionId);

	long countStudentsBySectionId(Integer sectionId);

	Iterable<Student> getAllStudent();

	Iterable<Student> searchStudentByLastName(String name);

	Iterable<Student> searchStudentByFirstName(String name);

	Iterable<Student> searchStudentByFirstAndLastName(String firstName, String lastName);

	boolean isLrnExist(Long lrn);
}

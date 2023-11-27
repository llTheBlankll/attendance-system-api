package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Student;

public interface StudentService {
	String addStudent(Student student);

	String deleteStudent(Student student);

	String deleteStudentById(Long id);

	Iterable<Student> getStudentByGradeLevel(String gradeLevel);
	Iterable<Student> existsAttendanceToday(Long lrn);

	Student getStudentById(Long lrn);

	String updateStudent(Student student);

	Iterable<Student> getAllStudentWithSectionId(Integer sectionId);

	long countStudentsBySectionId(Integer sectionId);

	Iterable<Student> getAllStudent();

	boolean studentExistsByLrn(Long lrn);
}

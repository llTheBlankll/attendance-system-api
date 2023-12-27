package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface StudentService {
	String createStudent(Student student);

	String deleteStudent(Student student);

	String deleteStudentById(Long id);

	Iterable<Student> getStudentByGradeLevel(Gradelevel gradeLevel);

	Student getStudentById(Long lrn);

	String updateStudent(Student student);

	Iterable<Student> getAllStudentWithSectionId(Integer sectionId);

	Page<Student> getAllStudent();

	Iterable<Student> searchStudentByLastName(String name);

	Iterable<Student> searchStudentByFirstName(String name);

	Iterable<Student> searchStudentByFirstAndLastName(String firstName, String lastName);

	Long countStudentsBySectionId(Integer sectionId);

	Long getAllStudentsCount();

	Long getAllStudentsCount(Gradelevel gradelevel);

	Long getAllStudentsCount(Strand strand);

	Long getAllStudentsCount(Section section);

	boolean isLrnExist(Long lrn);

	Boolean isStudentAttended(Long lrn, LocalDate date);
}

package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Student;

import java.util.List;

public interface GradeLevelService {
	Iterable<Gradelevel> getAllGradeLevel();

	String addGradeLevel(Gradelevel gradelevel);

	String deleteGradeLevel(Gradelevel gradelevel);

	String deleteGradeLevelById(Integer id);

	String updateGradeLevel(Gradelevel gradelevel);

	Iterable<Gradelevel> searchGradeLevelByName(String name);

	List<Student> getAllStudentByGradeLevel(String gradeName);
}

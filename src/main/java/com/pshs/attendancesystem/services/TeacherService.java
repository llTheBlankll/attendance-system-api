package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Teacher;

public interface TeacherService {
	void deleteTeacher(Integer teacherId);

	boolean createTeacher(Teacher teacher);

	Teacher getTeacher(Integer teacherId);

	Iterable<Teacher> getTeacherByLastName(String lastName);

	void updateTeacher(Teacher teacher);

	Iterable<Teacher> getAllTeachers();
}

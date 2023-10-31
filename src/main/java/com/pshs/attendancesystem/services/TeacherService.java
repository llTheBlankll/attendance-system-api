package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Subject;
import com.pshs.attendancesystem.entities.Teacher;

public interface TeacherService {

    void deleteTeacher(Long teacherId);

    boolean createTeacher(Teacher teacher);

    Teacher getTeacher(Long teacherId);

    Iterable<Teacher> getTeacherByLastName(String lastName);

    Iterable<Teacher> getTeacherBySubjectExpertise(Subject subject);

    void updateTeacher(Teacher teacher);

}

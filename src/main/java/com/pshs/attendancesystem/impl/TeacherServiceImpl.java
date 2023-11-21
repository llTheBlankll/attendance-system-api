package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Teacher;
import com.pshs.attendancesystem.messages.TeacherMessages;
import com.pshs.attendancesystem.repositories.TeacherRepository;
import com.pshs.attendancesystem.services.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public TeacherServiceImpl(TeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

	@Override
	public void deleteTeacher(Integer teacherId) {
		if (!this.teacherRepository.existsById(teacherId)) {
			logger.info("Teacher with ID {} doesn't exists.", teacherId);
			return;
		}

		this.teacherRepository.deleteById(teacherId);
	}

	@Override
	public boolean createTeacher(Teacher teacher) {
		if (teacher.getId() == null) {
			logger.info(TeacherMessages.TEACHER_NULL);
			return false;
		}

		this.teacherRepository.save(teacher);
		return true;
	}

	@Override
	public Teacher getTeacher(Integer teacherId) {
		Optional<Teacher> teacher = this.teacherRepository.findById(teacherId);
		return teacher.orElseGet(Teacher::new);
	}

	@Override
	public Iterable<Teacher> getTeacherByLastName(String lastName) {
		if (lastName.isEmpty()) {
			return Collections.emptyList();
		}
		return this.teacherRepository.findTeachersByLastNameIgnoreCase(lastName);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		if (teacher.getId() == null) {
			logger.info(TeacherMessages.TEACHER_NULL);
		}

		this.teacherRepository.save(teacher);
	}

	@Override
	public Iterable<Teacher> getAllTeachers() {
		return this.teacherRepository.findAll();
	}
}

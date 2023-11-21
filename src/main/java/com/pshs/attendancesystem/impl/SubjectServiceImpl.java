package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Subject;
import com.pshs.attendancesystem.messages.SubjectMessages;
import com.pshs.attendancesystem.repositories.SubjectRepository;
import com.pshs.attendancesystem.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {

	private final SubjectRepository subjectRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public SubjectServiceImpl(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	@Override
	public void createSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return;
		}

		// Check if subject already exists
		if (this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_ALREADY_EXISTS);
			return;
		}

		this.subjectRepository.save(subject);
	}

	@Override
	public Subject getSubject(Integer subjectId) {
		return this.subjectRepository.findById(subjectId).orElseGet(Subject::new);
	}

	@Override
	public Iterable<Subject> searchSubjectsByName(String subjectName) {
		return this.subjectRepository.findSubjectsByNameIgnoreCaseContaining(subjectName);
	}

	@Override
	public Subject getSubjectByName(String subjectName) {
		return this.subjectRepository.findSubjectByNameIgnoreCase(subjectName);
	}

	@Override
	public Iterable<Subject> searchSubjectsByDescription(String subjectDescription) {
		return this.subjectRepository.findSubjectsByDescriptionIgnoreCaseContaining(subjectDescription);
	}

	@Override
	public void deleteSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return;
		}

		if (!this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
			return;  // Subject doesn't exist. Nothing to delete.
		}

		this.subjectRepository.delete(subject);
	}

	@Override
	public void updateSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return;
		}

		if (!this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
			return;
		}

		this.subjectRepository.save(subject);
	}

	@Override
	public void deleteSubjectById(Integer subjectId) {
		if (subjectId < 0) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return;
		}


		if (!this.subjectRepository.existsById(subjectId)) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
		}

		this.subjectRepository.deleteById(subjectId);
	}

	@Override
	public Iterable<Subject> getAllSubjects() {
		return this.subjectRepository.findAll();
	}
}

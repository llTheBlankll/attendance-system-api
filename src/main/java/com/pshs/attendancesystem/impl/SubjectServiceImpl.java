package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Subject;
import com.pshs.attendancesystem.messages.SubjectMessages;
import com.pshs.attendancesystem.repositories.SubjectRepository;
import com.pshs.attendancesystem.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {
	"subject"
})
public class SubjectServiceImpl implements SubjectService {

	private final SubjectRepository subjectRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public SubjectServiceImpl(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	@Override
	public String createSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return SubjectMessages.SUBJECT_NULL;
		}

		// Check if subject already exists
		if (this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_ALREADY_EXISTS);
			return SubjectMessages.SUBJECT_ALREADY_EXISTS;
		}

		this.subjectRepository.save(subject);
		return SubjectMessages.SUBJECT_CREATED;
	}

	@Override
	@Cacheable(key = "#subjectId")
	public Subject getSubject(Integer subjectId) {
		return this.subjectRepository.findById(subjectId).orElseGet(Subject::new);
	}

	@Override
	@Cacheable(key = "#subjectName")
	public Iterable<Subject> searchSubjectsByName(String subjectName) {
		return this.subjectRepository.searchSubjectsByName(subjectName);
	}

	@Override
	@Cacheable(key = "#subjectName")
	public Subject getSubjectByName(String subjectName) {
		return this.subjectRepository.getSubjectByName(subjectName);
	}

	@Override
	@Cacheable(key = "#subjectDescription")
	public Iterable<Subject> searchSubjectsByDescription(String subjectDescription) {
		return this.subjectRepository.searchSubjectsByDescription(subjectDescription);
	}

	@Override
	public String deleteSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return SubjectMessages.SUBJECT_NULL;
		}

		if (!this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
			return SubjectMessages.SUBJECT_NOT_FOUND;
		}

		this.subjectRepository.delete(subject);
		return SubjectMessages.SUBJECT_DELETED;
	}

	@Override
	public String updateSubject(Subject subject) {
		if (subject.getId() == null) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return SubjectMessages.SUBJECT_NULL;
		}

		if (!this.subjectRepository.existsById(subject.getId())) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
			return SubjectMessages.SUBJECT_NOT_FOUND;
		}

		this.subjectRepository.save(subject);
		return SubjectMessages.SUBJECT_UPDATED;
	}

	@Override
	public String deleteSubjectById(Integer subjectId) {
		if (subjectId < 0) {
			logger.info(SubjectMessages.SUBJECT_NULL);
			return SubjectMessages.SUBJECT_NULL;
		}


		if (!this.subjectRepository.existsById(subjectId)) {
			logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
			return SubjectMessages.SUBJECT_NOT_FOUND;
		}

		this.subjectRepository.deleteById(subjectId);
		return SubjectMessages.SUBJECT_DELETED;
	}

	@Override
	public Iterable<Subject> getAllSubjects() {
		return this.subjectRepository.findAll();
	}
}

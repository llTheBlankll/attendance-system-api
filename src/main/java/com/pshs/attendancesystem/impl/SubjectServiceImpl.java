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
    public void deleteSubject(Integer subjectId) {
        if (!this.subjectRepository.existsById(subjectId)) {
            logger.info("Subject with ID {} doesn't exists.", subjectId);
            return;
        }
        this.subjectRepository.deleteById(subjectId);
        logger.info("Subject with ID {} deleted.", subjectId);
    }

    @Override
    public Integer createSubject(Subject subject) {
        if (subject.getId() == null) {
            logger.info(SubjectMessages.SUBJECT_NULL);
            return null;
        }

        // Check if subject already exists
        if (this.subjectRepository.existsById(subject.getId())) {
            logger.info(SubjectMessages.SUBJECT_ALREADY_EXISTS);
            return 0;
        }

        this.subjectRepository.save(subject);
        return subject.getId();
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
    public Subject updateSubject(Subject subject) {
        if (subject.getId() == null) {
            logger.info(SubjectMessages.SUBJECT_NULL);
            return null;
        }

        if (!this.subjectRepository.existsById(subject.getId())) {
            logger.info(SubjectMessages.SUBJECT_NOT_FOUND);
            return null;  // Subject doesn't exist. Nothing to update.
        }

        return this.subjectRepository.save(subject);
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
}

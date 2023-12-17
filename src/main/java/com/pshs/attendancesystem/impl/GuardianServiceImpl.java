package com.pshs.attendancesystem.impl;


import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.messages.GuardianMessages;
import com.pshs.attendancesystem.repositories.GuardianRepository;
import com.pshs.attendancesystem.services.GuardianService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GuardianServiceImpl implements GuardianService {

	private final GuardianRepository guardianRepository;

	public GuardianServiceImpl(GuardianRepository guardianRepository) {
		this.guardianRepository = guardianRepository;
	}

	@Override
	@Cacheable(value = "guardian")
	public long getGuardiansCount() {
		return guardianRepository.count();
	}

	@Override
	@Cacheable(value = "guardian", key = "#student.lrn")
	public Iterable<Guardian> getStudentGuardians(Student student) {
		if (student.getLrn() != null) {
			return this.guardianRepository.getStudentGuardians(student);
		}

		return Collections.emptyList();
	}

	@Override
	@Cacheable(value = "guardian", key = "#lrn")
	public Iterable<Guardian> getStudentGuardiansByLrn(Long lrn) {
		if (lrn == null) {
			return Collections.emptyList();
		}

		if (this.guardianRepository.isLrnExist(lrn)) {
			return this.guardianRepository.getGuardiansByLrn(lrn);
		}

		return Collections.emptyList();
	}

	@Override
	@Cacheable(value = "guardian", key = "#fullName")
	public Iterable<Guardian> searchGuardianByFullName(String fullName) {
		if (fullName.isEmpty()) {
			return Collections.emptyList();
		}

		return this.guardianRepository.searchGuardiansByFullName(fullName);
	}

	@Override
	@CachePut(value = "guardian", key = "#guardian.id")
	public boolean createGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.save(guardian);
			return true;
		}

		return false;
	}

	@Override
	@CacheEvict(value = "guardian", key = "#guardian.id")
	public boolean deleteGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.delete(guardian);
			return true;
		}

		return false;
	}

	@Override
	@CacheEvict(value = "guardian", key = "#guardianId")
	public String deleteGuardianById(Integer guardianId) {
		if (guardianId == null) {
			return GuardianMessages.GUARDIAN_NULL;
		} else if (!guardianRepository.existsById(guardianId)) {
			return GuardianMessages.GUARDIAN_NOT_FOUND;
		}

		guardianRepository.deleteById(guardianId);
		return GuardianMessages.GUARDIAN_DELETED;
	}

	@Override
	@CachePut(value = "guardian", key = "#guardian.id")
	public boolean updateGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.save(guardian);
			return true;
		}

		return false;
	}
}

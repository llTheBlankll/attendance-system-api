package com.pshs.attendancesystem.impl;


import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.GuardianRepository;
import com.pshs.attendancesystem.services.GuardianService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GuardianServiceImpl implements GuardianService {

	private final GuardianRepository guardianRepository;

	public GuardianServiceImpl(GuardianRepository guardianRepository) {
		this.guardianRepository = guardianRepository;
	}

	@Override
	public long getGuardiansCount() {
		return guardianRepository.count();
	}

	@Override
	public Iterable<Guardian> getStudentGuardians(Student student) {
		if (student.getLrn() != null) {
			return this.guardianRepository.findByStudent(student);
		}

		return Collections.emptyList();
	}

	@Override
	public Iterable<Guardian> getStudentGuardiansByLrn(Long lrn) {
		if (lrn == null) {
			return Collections.emptyList();
		}

		if (this.guardianRepository.existsByStudentLrn(lrn)) {
			return this.guardianRepository.findByStudentLrn(lrn);
		}

		return Collections.emptyList();
	}

	@Override
	public Iterable<Guardian> searchGuardianByFullName(String fullName) {
		if (fullName.isEmpty()) {
			return Collections.emptyList();
		}

		return this.guardianRepository.findGuardiansByFullNameIgnoreCase(fullName);
	}

	@Override
	public boolean createGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.save(guardian);
			return true;
		}

		return false;
	}

	@Override
	public boolean deleteGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.delete(guardian);
			return true;
		}

		return false;
	}

	@Override
	public void deleteGuardianById(Integer guardianId) {
		this.guardianRepository.deleteById(guardianId);
	}

	@Override
	public boolean updateGuardian(Guardian guardian) {
		if (guardian.getStudent().getLrn() != null) {
			this.guardianRepository.save(guardian);
			return true;
		}

		return false;
	}
}

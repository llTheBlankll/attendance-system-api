package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.messages.GradeLevelMessages;
import com.pshs.attendancesystem.repositories.GradeLevelRepository;
import com.pshs.attendancesystem.services.GradeLevelService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class GradeLevelServiceImpl implements GradeLevelService {
	private final GradeLevelRepository gradeLevelRepository;

	public GradeLevelServiceImpl(GradeLevelRepository gradeLevelRepository) {
		this.gradeLevelRepository = gradeLevelRepository;
	}

	@Override
	public Iterable<Gradelevel> getAllGradeLevel() {
		return gradeLevelRepository.findAll();
	}

	@Override
	@CachePut(value = "gradelevel", key = "#gradelevel.id")
	public String addGradeLevel(Gradelevel gradelevel) {
		if (!gradelevel.getGradeName().isEmpty()) {
			gradeLevelRepository.save(gradelevel);
			return GradeLevelMessages.GRADELEVEL_CREATED;
		}

		return GradeLevelMessages.GRADELEVEL_EMPTY;
	}

	@Override
	@CacheEvict(value = "gradelevel", key = "#gradelevel.id")
	public String deleteGradeLevel(Gradelevel gradelevel) {
		if (gradelevel.getId() != null && this.gradeLevelRepository.existsById(gradelevel.getId())) {
			this.gradeLevelRepository.delete(gradelevel);
			return GradeLevelMessages.GRADELEVEL_DELETED;
		}

		return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
	}

	@Override
	@CacheEvict(value = "gradelevel", key = "#id")
	public String deleteGradeLevelById(Integer id) {
		if (id != null && this.gradeLevelRepository.existsById(id)) {
			this.gradeLevelRepository.deleteById(id);
			return GradeLevelMessages.GRADELEVEL_DELETED;
		}

		return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
	}

	@Override
	@CachePut(value = "gradelevel", key = "#gradelevel.id")
	public String updateGradeLevel(Gradelevel gradelevel) {
		if (gradelevel.getGradeName().isEmpty()) {
			return GradeLevelMessages.GRADELEVEL_EMPTY;
		}

		this.gradeLevelRepository.save(gradelevel);
		return GradeLevelMessages.GRADELEVEL_UPDATED;
	}
}

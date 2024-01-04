package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.messages.GradeLevelMessages;
import com.pshs.attendancesystem.messages.StrandMessages;
import com.pshs.attendancesystem.repositories.GradeLevelRepository;
import com.pshs.attendancesystem.repositories.StrandRepository;
import com.pshs.attendancesystem.services.GradeLevelService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"gradelevel"})
public class GradeLevelServiceImpl implements GradeLevelService {
	private final GradeLevelRepository gradeLevelRepository;
	private final StrandRepository strandRepository;

	public GradeLevelServiceImpl(GradeLevelRepository gradeLevelRepository, StrandRepository strandRepository) {
		this.gradeLevelRepository = gradeLevelRepository;
		this.strandRepository = strandRepository;
	}

	@Override
	public Iterable<Gradelevel> getAllGradeLevel() {
		return gradeLevelRepository.findAll();
	}

	@Override
	@CachePut(key = "#gradelevel.id")
	public String addGradeLevel(Gradelevel gradelevel) {
		if (!gradelevel.getGradeName().isEmpty()) {
			gradeLevelRepository.save(gradelevel);
			return GradeLevelMessages.GRADELEVEL_CREATED;
		}

		return GradeLevelMessages.GRADELEVEL_EMPTY;
	}

	@Override
	@CacheEvict(key = "#gradelevel.id")
	public String deleteGradeLevel(Gradelevel gradelevel) {
		if (gradelevel.getId() != null && this.gradeLevelRepository.existsById(gradelevel.getId())) {
			this.gradeLevelRepository.delete(gradelevel);
			return GradeLevelMessages.GRADELEVEL_DELETED;
		}

		return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
	}

	@Override
	@CacheEvict(key = "#id")
	public String deleteGradeLevelById(Integer id) {
		if (id != null && this.gradeLevelRepository.existsById(id)) {
			this.gradeLevelRepository.deleteById(id);
			return GradeLevelMessages.GRADELEVEL_DELETED;
		}

		return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
	}

	@Override
	@CachePut(key = "#gradelevel.id")
	public String updateGradeLevel(Gradelevel gradelevel) {
		if (gradelevel.getGradeName().isEmpty()) {
			return GradeLevelMessages.GRADELEVEL_EMPTY;
		}

		this.gradeLevelRepository.save(gradelevel);
		return GradeLevelMessages.GRADELEVEL_UPDATED;
	}

	@Override
	public String updateGradeLevelWithStrand(Gradelevel gradelevel, Integer strandId) {
		// @ Check if gradelevel and strand exists
		if (gradeLevelRepository.existsById(gradelevel.getId())) {
			// @ Check if strand exists
			if (strandRepository.existsById(strandId)) {

				// @ Check if Present
				Optional<Strand> strandOptional = strandRepository.findById(strandId);
				if (strandOptional.isPresent()) {
					gradelevel.setStrand(strandRepository.findById(strandId).get());
					this.gradeLevelRepository.save(gradelevel);
					return GradeLevelMessages.GRADELEVEL_UPDATED;
				}
			} else {
				return StrandMessages.STRAND_NOT_FOUND;
			}
		}

		return GradeLevelMessages.GRADELEVEL_NOT_FOUND;
	}

	@Override
	@Cacheable(key = "#name")
	public Iterable<Gradelevel> searchGradeLevelByName(String name) {
		return gradeLevelRepository.searchGradeLevelByName(name);
	}

	@Override
	public List<Student> getAllStudentByGradeLevel(String gradeName) {
		Optional<Gradelevel> gradelevelOptional = gradeLevelRepository.getAllStudentByGradeName(gradeName);

		// check if gradelevel exists
		if (gradelevelOptional.isPresent()) {
			// get students
			Gradelevel gradelevel = gradelevelOptional.get();
			return gradelevel.getStudents();
		} else {
			return Collections.emptyList();
		}
	}
}

package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.messages.StrandMessages;
import com.pshs.attendancesystem.repositories.GradeLevelRepository;
import com.pshs.attendancesystem.repositories.StrandRepository;
import com.pshs.attendancesystem.services.StrandService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {
	"strand"
})
public class StrandServiceImpl implements StrandService {

	private final StrandRepository strandRepository;
	private final GradeLevelRepository gradeLevelRepository;

	public StrandServiceImpl(StrandRepository strandRepository, GradeLevelRepository gradeLevelRepository) {
		this.strandRepository = strandRepository;
		this.gradeLevelRepository = gradeLevelRepository;
	}

	@Override
	public Iterable<Strand> getAllStrand() {
		return this.strandRepository.findAll();
	}

	@Override
	public String createStrand(Strand strand) {
		if (strand.getStrandName().isEmpty()) {
			return StrandMessages.STRAND_NO_NAME;
		}

		if (strandRepository.isStrandNameExist(strand.getStrandName())) {
			return StrandMessages.STRAND_EXISTS;
		}

		this.strandRepository.save(strand);
		return StrandMessages.STRAND_CREATED(strand.getStrandName());
	}

	@Override
	public String deleteStrand(Strand strand) {
		if (strand.getId() == null) {
			return StrandMessages.STRAND_NULL;
		}

		if (!this.strandRepository.existsById(strand.getId())) {
			return StrandMessages.STRAND_NOT_FOUND;
		}

		this.strandRepository.delete(strand);
		return StrandMessages.STRAND_DELETED;
	}

	@Override
	@Cacheable(key = "#strandName")
	public Iterable<Strand> searchStrandByName(@NonNull String strandName) {
		return this.strandRepository.searchStrandName(strandName);
	}

	@Override
	public String updateStrand(Strand strand) {
		if (strand.getId() == null) {
			return StrandMessages.STRAND_NULL;
		}

		if (!this.strandRepository.existsById(strand.getId())) {
			return StrandMessages.STRAND_NOT_FOUND;
		}

		this.strandRepository.save(strand);
		return StrandMessages.STRAND_UPDATED;
	}

	@Override
	public String updateGradeLevelWithStrand(Strand strand, Integer gradeLevelId) {
		if (strandRepository.existsById(strand.getId())) {
			if (gradeLevelRepository.updateStrandById(strand, gradeLevelId) > 0) {
				return StrandMessages.STRAND_UPDATED;
			}

			return StrandMessages.STRAND_NO_NAME;
		} else {
			return StrandMessages.STRAND_NOT_FOUND;
		}
	}
}

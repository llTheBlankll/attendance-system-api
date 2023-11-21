package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.messages.StrandMessages;
import com.pshs.attendancesystem.repositories.StrandRepository;
import com.pshs.attendancesystem.services.StrandService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class StrandServiceImpl implements StrandService {

	private final StrandRepository strandRepository;

	public StrandServiceImpl(StrandRepository strandRepository) {
		this.strandRepository = strandRepository;
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

		if (strandRepository.existsByStrandName(strand.getStrandName())) {
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
	public Iterable<Strand> searchStrandByName(@NonNull String strandName) {
		return this.strandRepository.findByStrandNameContainsIgnoreCase(strandName);
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
}

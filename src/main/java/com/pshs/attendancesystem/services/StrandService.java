package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Strand;

public interface StrandService {
	Iterable<Strand> getAllStrand();

	String createStrand(Strand strand);

	String deleteStrand(Strand strand);

	Iterable<Strand> searchStrandByName(String strandName);

	String updateStrand(Strand strand);

	String updateGradeLevelWithStrand(Strand strand, Integer gradeLevelId);
}

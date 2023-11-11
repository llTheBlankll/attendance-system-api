package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.repositories.StrandRepository;
import com.pshs.attendancesystem.services.StrandService;
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
}

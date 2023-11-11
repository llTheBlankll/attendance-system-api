package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.services.RfidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RfidCredentialsServiceImpl implements RfidService {

    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final Logger logger = LoggerFactory.getLogger(RfidCredentialsServiceImpl.class);

    public RfidCredentialsServiceImpl(RfidCredentialsRepository rfidCredentialsRepository) {
        this.rfidCredentialsRepository = rfidCredentialsRepository;
    }

    @Override
    public Iterable<RfidCredentials> getAllRfidCredentials() {
        return this.rfidCredentialsRepository.findAll();
    }

    @Override
    public Optional<RfidCredentials> getRfidCredentialByStudentLrn(Long lrn) {
        if (!this.rfidCredentialsRepository.existsById(lrn)) {
            logger.info(RfidMessages.LRN_NOT_FOUND);
            return null;
        }
        return this.rfidCredentialsRepository.findByLrn(lrn);
    }

    @Override
    public Optional<RfidCredentials> getRfidCredentialByHashedLrn(String hashedLrn) {
        if (!this.rfidCredentialsRepository.existsByHashedLrn(hashedLrn)) {
            logger.info(RfidMessages.HASHED_LRN_NOT_FOUND);
            return null;
        }

        return this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);
    }
}

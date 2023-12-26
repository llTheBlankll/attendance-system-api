package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.services.RfidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = {
	"rfid"
})
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
	@Cacheable(key = "#lrn")
	public Optional<RfidCredentials> getRfidCredentialByStudentLrn(Long lrn) {
		if (lrn == null) {
			return Optional.empty();
		}

		if (!this.rfidCredentialsRepository.existsById(lrn)) {
			logger.info(RfidMessages.LRN_NOT_FOUND);
			return Optional.empty();
		}

		return this.rfidCredentialsRepository.findByLrn(lrn);
	}

	@Override
	@Cacheable(key = "#hashedLrn")
	public Optional<RfidCredentials> getRfidCredentialByHashedLrn(String hashedLrn) {
		if (hashedLrn == null) {
			return Optional.empty();
		}

		if (!this.rfidCredentialsRepository.isHashedLrnExist(hashedLrn)) {
			logger.info(RfidMessages.HASHED_LRN_NOT_FOUND);
			return Optional.empty();
		}

		return this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);
	}

	@Override
	public boolean isHashedLrnExist(String hashedLrn) {
		return this.rfidCredentialsRepository.isHashedLrnExist(hashedLrn);
	}

	@Override
	public boolean isLrnExist(Long lrn) {
		return rfidCredentialsRepository.isLrnExist(lrn);
	}
}

package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Fingerprint;
import com.pshs.attendancesystem.messages.FingerprintMessages;
import com.pshs.attendancesystem.repositories.FingerprintRepository;
import com.pshs.attendancesystem.services.FingerprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FingerprintServiceImpl implements FingerprintService {

	private final FingerprintRepository fingerprintRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public FingerprintServiceImpl(FingerprintRepository fingerprintRepository) {
		this.fingerprintRepository = fingerprintRepository;
	}

	@Override
	@Cacheable(value = "fingerprint", key = "#fingerprintId")
	public Fingerprint getFingerprintByFingerprintId(@NonNull String fingerprintId) {
		return this.fingerprintRepository.getFingerprintId(fingerprintId).orElse(null);
	}

	@Override
	@Cacheable(value = "fingerprint", key = "#lrn")
	public Fingerprint getFingerprintByStudentLrn(Long lrn) {
		return this.fingerprintRepository.getStudentLrn(lrn).orElse(null);
	}

	@Override
	@Cacheable(value = "fingerprint")
	public Iterable<Fingerprint> getAllFingerprint() {
		return this.fingerprintRepository.findAll();
	}

	@Override
	@CachePut(value = "fingerprint", key = "#fingerprint.fingerprintId")
	public String addFingerprint(@NonNull Fingerprint fingerprint) {
		if (fingerprint.getFingerprintId() != null) {
			this.fingerprintRepository.save(fingerprint);
			return FingerprintMessages.FINGERPRINT_CREATED;
		}

		return FingerprintMessages.FINGERPRINT_INVALID;
	}

	@Override
	@CacheEvict(value = "fingerprint", key = "#fingerprint.fingerprintId")
	public String deleteFingerprint(@NonNull Fingerprint fingerprint) {
		try {
			if (fingerprint.getFingerprintId() != null) {
				this.fingerprintRepository.delete(fingerprint);
				return FingerprintMessages.FINGERPRINT_DELETED;
			}

			return FingerprintMessages.FINGERPRINT_NOT_FOUND;
		} catch (Exception e) {
			logger.info(FingerprintMessages.FINGERPRINT_NOT_FOUND);
			return FingerprintMessages.FINGERPRINT_NOT_FOUND;
		}
	}

	@Override
	@CacheEvict(value = "fingerprint", key = "#fingerprintId")
	public String deleteFingerprintByFingerprintId(String fingerprintId) {
		return this.fingerprintRepository.getFingerprintId(fingerprintId).map(
			fingerprint -> {
				this.fingerprintRepository.delete(fingerprint);
				return FingerprintMessages.FINGERPRINT_DELETED;
			}
		).orElse(FingerprintMessages.FINGERPRINT_NOT_FOUND);
	}

	@Override
	@CacheEvict(value = "fingerprint", key = "#lrn")
	public String deleteFingerprintByStudentLrn(Long lrn) {
		return this.fingerprintRepository.getStudentLrn(lrn).map(
			fingerprint -> {
				this.fingerprintRepository.delete(fingerprint);
				return FingerprintMessages.FINGERPRINT_DELETED;
			}
		).orElse(FingerprintMessages.FINGERPRINT_NOT_FOUND);
	}

	@Override
	@CachePut(value = "fingerprint", key = "#fingerprint.fingerprintId")
	public String updateFingerprintByFingerprintId(Fingerprint fingerprint) {
		return this.fingerprintRepository.getFingerprintId(fingerprint.getFingerprintId()).map(
			existingFingerprint -> {
				this.fingerprintRepository.save(fingerprint);
				return FingerprintMessages.FINGERPRINT_UPDATED;
			}
		).orElseGet(() -> {
			logger.info(FingerprintMessages.FINGERPRINT_NOT_FOUND);
			return FingerprintMessages.FINGERPRINT_NOT_FOUND;
		});
	}
}

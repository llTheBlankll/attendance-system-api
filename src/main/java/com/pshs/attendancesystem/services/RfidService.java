package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.RfidCredentials;

import java.util.Optional;

public interface RfidService {
	Iterable<RfidCredentials> getAllRfidCredentials();

	Optional<RfidCredentials> getRfidCredentialByStudentLrn(Long lrn);

	Optional<RfidCredentials> getRfidCredentialByHashedLrn(String hashedLrn);

	boolean toggleRfidStatus(Long lrn);

	boolean toggleRfidStatus(String hashedLrn);

	boolean isRfidEnabled(Long lrn);

	boolean isRfidEnabled(String hashedLrn);

	boolean isHashedLrnExist(String hashedLrn);

	boolean isLrnExist(Long lrn);
}

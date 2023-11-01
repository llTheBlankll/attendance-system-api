package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.RfidCredentials;

public interface RfidService {
    Iterable<RfidCredentials> getAllRfidCredentials();

    RfidCredentials getRfidCredentialByStudentLrn(Long lrn);

    RfidCredentials getRfidCredentialByHashedLrn(String hashedLrn);
}

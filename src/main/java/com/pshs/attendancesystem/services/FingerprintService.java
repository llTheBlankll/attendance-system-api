package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.entities.Fingerprint;

public interface FingerprintService {
    Fingerprint getFingerprintByFingerprintId(String fingerprintId);

    Fingerprint getFingerprintByStudentLrn(Long lrn);

    Iterable<Fingerprint> getAllFingerprint();

    String addFingerprint(Fingerprint fingerprint);

    String deleteFingerprint(Fingerprint fingerprint);

    String deleteFingerprintByFingerprintId(String fingerprintId);

    String deleteFingerprintByStudentLrn(Long lrn);

    String updateFingerprintByFingerprintId(Fingerprint fingerprintId);
}

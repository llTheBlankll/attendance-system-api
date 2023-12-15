package com.pshs.attendancesystem.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link org.bouncycastle.util.Fingerprint}
 */
public class FingerprintDto implements Serializable {
	private final byte[] fingerprint;

	public FingerprintDto(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	public byte[] getFingerprint() {
		return fingerprint;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FingerprintDto entity = (FingerprintDto) o;
		return Objects.equals(this.fingerprint, entity.fingerprint);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fingerprint);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"fingerprint = " + fingerprint + ")";
	}
}
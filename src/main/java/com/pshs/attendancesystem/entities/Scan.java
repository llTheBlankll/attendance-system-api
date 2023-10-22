package com.pshs.attendancesystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "scan")
public class Scan {
    @Id
    @Column(name = "lrn", nullable = false)
    private Long lrn;

    @Column(name = "hashed_lrn", length = 128)
    private String hashedLrn;

    @Column(name = "salt", length = 32)
    private String salt;

    public Long getLrn() {
        return lrn;
    }

    public void setLrn(Long lrn) {
        this.lrn = lrn;
    }

    public String getHashedLrn() {
        return hashedLrn;
    }

    public void setHashedLrn(String hashedLrn) {
        this.hashedLrn = hashedLrn;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
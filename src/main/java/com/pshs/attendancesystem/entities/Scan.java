package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "scan")
public class Scan {
    @Id
    @Column(name = "lrn", nullable = false)
    private Long lrn;

    @Column(name = "hashed_lrn", length = 128)
    private String hashedLrn;

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

}
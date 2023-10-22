package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "scan")
public class Scan {
    @Id
    @Column(name = "lrn", nullable = false)
    private Long lrn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lrn", referencedColumnName = "lrn", nullable = false)
    @JsonManagedReference
    private Student student;

    @Column(name = "hashed_lrn", length = 128)
    private String hashedLrn;

    @Column(name = "salt", length = 32)
    private String salt;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

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
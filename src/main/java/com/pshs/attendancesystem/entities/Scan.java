package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "scan")
public class Scan {
    @Id
    @Column(name = "lrn", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lrn", nullable = false)
    private Student student;

    @Column(name = "hashed_lrn", length = 128)
    private String hashedLrn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getHashedLrn() {
        return hashedLrn;
    }

    public void setHashedLrn(String hashedLrn) {
        this.hashedLrn = hashedLrn;
    }

}
package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "gradelevels")
public class Gradelevel {
    @Id
    @Column(name = "grade_level", nullable = false)
    private Integer id;

    @Column(name = "grade_name", nullable = false)
    private String gradeName;

    @OneToOne(mappedBy = "gradeLevel", cascade = CascadeType.MERGE)
    private Student student;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

}
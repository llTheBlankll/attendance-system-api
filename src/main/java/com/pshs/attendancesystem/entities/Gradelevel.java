package com.pshs.attendancesystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gradelevels")
public class Gradelevel {
    @Id
    @Column(name = "grade_level", nullable = false)
    private Integer id;

    @Column(name = "grade_name", nullable = false)
    private String gradeName;

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
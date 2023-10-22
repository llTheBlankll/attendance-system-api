package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    private String sectionId;

    @Column(name = "adviser")
    private String adviser;

    @Column(name = "room")
    private Integer room;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "grade_level")
    private Gradelevel gradeLevel;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @OneToMany(mappedBy = "studentSection", cascade = CascadeType.MERGE)
    private Set<Student> students = new LinkedHashSet<>();

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getAdviser() {
        return adviser;
    }

    public void setAdviser(String adviser) {
        this.adviser = adviser;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Gradelevel getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Gradelevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

}
package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "sections")
public class GradeLevels {

    @Id
    @Column(name = "section_id", nullable = false, length = 2)
    private String sectionId;

    @Column(name = "adviser")
    private String adviser;

    @Column(name = "room")
    private Integer room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grade_level", nullable = false)
    private Gradelevel gradeLevel;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Gradelevel getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Gradelevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public String getAdviser() {
        return adviser;
    }

    public void setAdviser(String adviser) {
        this.adviser = adviser;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}

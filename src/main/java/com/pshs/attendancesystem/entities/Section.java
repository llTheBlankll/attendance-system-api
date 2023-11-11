package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id", nullable = false)
    private Integer sectionId;

    @Column(name = "room")
    private Integer room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_level")
    private Gradelevel gradeLevel;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "teacher")
    private Teacher teacher;

    @OneToMany(mappedBy = "studentSection", cascade = CascadeType.MERGE, targetEntity = Student.class, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Student> students;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Strand.class, cascade = CascadeType.DETACH)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "strand")
    private Strand strand;

    public Strand getStrand() {
        return strand;
    }

    public void setStrand(Strand strand) {
        this.strand = strand;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
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
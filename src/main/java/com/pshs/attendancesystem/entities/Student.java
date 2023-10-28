package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "lrn", nullable = false)
    private Long lrn;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Gradelevel.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "grade_level")
    private Gradelevel studentGradeLevel;

    @Column(name = "sex", length = 6)
    private String sex;

    @ManyToOne(targetEntity = Section.class, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id")
    private Section studentSection;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, targetEntity = Guardian.class, fetch = FetchType.EAGER)
    private Set<Guardian> guardian;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, targetEntity = Attendance.class, fetch = FetchType.EAGER)
    private Set<Attendance> attendances;

    @OneToOne(targetEntity = RfidCredentials.class, mappedBy = "student", cascade = CascadeType.REMOVE)
    @JoinColumn(name = "lrn")
    @JsonBackReference
    private RfidCredentials studentRfidCredentials;

    public Set<Guardian> getGuardian() {
        return guardian;
    }

    public void setGuardian(Set<Guardian> guardian) {
        this.guardian = guardian;
    }

    public RfidCredentials getStudentRfidCredentials() {
        return studentRfidCredentials;
    }

    public void setStudentRfidCredentials(RfidCredentials studentRfidCredentials) {
        this.studentRfidCredentials = studentRfidCredentials;
    }

    public Long getLrn() {
        return lrn;
    }

    public void setLrn(Long lrn) {
        this.lrn = lrn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gradelevel getStudentGradeLevel() {
        return studentGradeLevel;
    }

    public void setStudentGradeLevel(Gradelevel studentGradeLevel) {
        this.studentGradeLevel = studentGradeLevel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Section getStudentSection() {
        return studentSection;
    }

    public void setStudentSection(Section studentSection) {
        this.studentSection = studentSection;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        this.attendances = attendances;
    }
}
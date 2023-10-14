package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Gradelevel.class)
    @JoinColumn(name = "grade_level")
    private Gradelevel studentGradeLevel;

    @Column(name = "sex", length = 6)
    private String sex;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Section.class)
    @JoinColumn(name = "section_id")
    private Section studentSection;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "contact_guardian")
    private Long contactGuardian;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Attendance> attendances = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lrn")
    private Scan studentScan;

    public Scan getStudentScan() {
        return studentScan;
    }

    public void setStudentScan(Scan studentScan) {
        this.studentScan = studentScan;
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

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public Long getContactGuardian() {
        return contactGuardian;
    }

    public void setContactGuardian(Long contactGuardian) {
        this.contactGuardian = contactGuardian;
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
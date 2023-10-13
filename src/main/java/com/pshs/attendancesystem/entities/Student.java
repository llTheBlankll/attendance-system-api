package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lrn", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Gradelevel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "grade_level")
    private Gradelevel gradeLevel;

    @Column(name = "sex", length = 6)
    private String sex;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Section.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "contact_guardian")
    private Long contactGuardian;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @OneToMany(mappedBy = "student")
    private Set<Attendance> attendances = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Gradelevel getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Gradelevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
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
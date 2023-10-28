package com.pshs.attendancesystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pshs.attendancesystem.enums.Relationship;
import jakarta.persistence.*;

@Entity
@Table(name = "guardians")
public class Guardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guardian_id", nullable = false)
    private Integer id;

    @JoinColumn(name = "student_lrn")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Student student;

    @Column(name = "first_name", length = 32)
    private String firstName;

    @Column(name = "middle_name", length = 32)
    private String middleName;

    @Column(name = "last_name", length = 32)
    private String lastName;

    @Column(name = "contact_number", length = 32)
    private String contactNumber;
    @Column(name = "relationship_to_student", columnDefinition = "ENUM ('FATHER', 'MOTHER', 'GUARDIAN', 'SIBLING', 'STEPFATHER', 'STEPMOTHER', 'GRANDPARENT', 'OTHER')")
    @Enumerated(EnumType.STRING)
    private Relationship relationshipToStudent;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Relationship getRelationshipToStudent() {
        return relationshipToStudent;
    }

    public void setRelationshipToStudent(Relationship relationshipToStudent) {
        this.relationshipToStudent = relationshipToStudent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

}
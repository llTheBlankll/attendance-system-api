package com.pshs.attendancesystem.entities;

import com.pshs.attendancesystem.Enums;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private Time time;

    @Enumerated(EnumType.STRING)
    Enums.status attendanceStatus;

    public Enums.status getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(Enums.status attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }


    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
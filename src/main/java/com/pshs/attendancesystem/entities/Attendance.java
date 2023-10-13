package com.pshs.attendancesystem.entities;

import com.pshs.attendancesystem.Enums;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalTime;
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
    private Date date;
    @Column(name = "time")
    private Time time;

    @Enumerated(EnumType.STRING)
    Enums.attendanceStatus attendance_status;

    public Enums.attendanceStatus getAttendance_status() {
        return attendance_status;
    }

    public void setAttendance_status(Enums.attendanceStatus attendance_status) {
        this.attendance_status = attendance_status;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
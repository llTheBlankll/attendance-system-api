package com.pshs.attendancesystem.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date")
    private LocalDate date;
    @Column(name = "\"time\"")
    private LocalTime time;

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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

/*
    TODO [JPA Buddy] create field to map the 'attendance_status' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "attendance_status", columnDefinition = "status(0, 0)")
    private Object attendanceStatus;
*/
}
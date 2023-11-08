package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateWithStatusLrn {

    private BetweenDate date;
    private Status status;
    private Long studentLrn;

    public BetweenDate getDate() {
        return date;
    }

    public void setDate(BetweenDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getStudentLrn() {
        return studentLrn;
    }

    public void setStudentLrn(Long studentLrn) {
        this.studentLrn = studentLrn;
    }
}

package com.pshs.attendancesystem.entities.statistics;

import com.pshs.attendancesystem.enums.Status;

public class DateWithStatus {

    private BetweenDate date;
    private Status status;

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
}

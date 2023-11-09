package com.pshs.attendancesystem.entities;

import com.pshs.attendancesystem.enums.Status;

import java.sql.Time;

public class WebSocketResponse {

    private String message;
    private Time time;
    private Long studentLrn;
    private Status status;
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Long getStudentLrn() {
        return studentLrn;
    }

    public void setStudentLrn(Long studentLrn) {
        this.studentLrn = studentLrn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WebSocketResponse{" +
            "message='" + message + '\'' +
            ", time=" + time +
            ", studentLrn=" + studentLrn +
            ", status=" + status +
            '}';
    }
}

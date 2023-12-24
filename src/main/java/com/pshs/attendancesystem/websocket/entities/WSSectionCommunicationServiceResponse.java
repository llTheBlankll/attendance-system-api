package com.pshs.attendancesystem.websocket.entities;

import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;

import java.time.LocalTime;

public class WSSectionCommunicationServiceResponse {

	private Student student;
	private Status status;
	private LocalTime time;

	public WSSectionCommunicationServiceResponse() {
	}

	public WSSectionCommunicationServiceResponse(Student student, Status status, LocalTime time) {
		this.student = student;
		this.status = status;
		this.time = time;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
}

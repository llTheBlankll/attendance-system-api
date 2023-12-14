package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.services.StudentService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;

@Component
public class StudentWebSocketHandler extends TextWebSocketHandler {

	private final StudentService studentService;

	public StudentWebSocketHandler(StudentService studentService) {
		this.studentService = studentService;
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		Student student = mapper.readValue(message.getPayload().toString(), Student.class);
		session.sendMessage(new TextMessage(studentService.addStudent(student)));
	}
}

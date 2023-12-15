package com.pshs.attendancesystem.websocket.handlers;

import com.pshs.attendancesystem.services.AttendanceService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SectionWebSocketHandler extends TextWebSocketHandler {
	private final AttendanceService attendanceService;

	public SectionWebSocketHandler(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
	}
}

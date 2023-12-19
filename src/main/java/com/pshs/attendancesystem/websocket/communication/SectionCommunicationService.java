package com.pshs.attendancesystem.websocket.communication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SectionCommunicationService {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private final List<WebSocketSession> sessions = new ArrayList<>();

	public void registerSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public void sendUpdate(String message) {
		for (WebSocketSession session : sessions) {
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				logger.error(e.getMessage());
				removeSession(session);
			}
		}
	}
}

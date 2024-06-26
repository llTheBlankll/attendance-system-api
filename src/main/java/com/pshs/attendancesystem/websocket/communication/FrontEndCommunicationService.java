package com.pshs.attendancesystem.websocket.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
public class FrontEndCommunicationService {

	private final List<WebSocketSession> sessionList = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void registerFrontEndWebSocket(WebSocketSession session) {
		sessionList.add(session);
	}

	@Async
	public void sendMessageToAllFrontEnd(String message) {
		try {
			for (WebSocketSession session : this.sessionList) {
				session.sendMessage(new TextMessage(message));
			}
		} catch (IOException exception) {
			logger.error(exception.getMessage());
		}
	}

	public void removeWebSocketSession(WebSocketSession session) {
		this.sessionList.remove(session);
	}
}

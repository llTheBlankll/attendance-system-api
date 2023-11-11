package com.pshs.attendancesystem.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrontEndWebSocketsCommunicationService {

    private final List<WebSocketSession> sessionList = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void registerFrontEndWebSocket(WebSocketSession session) {
        sessionList.add(session);
    }

    public void sendMessageToAllFrontEnd(String message) {
        logger.info("Sending Front End Servers Message: {}", message);
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

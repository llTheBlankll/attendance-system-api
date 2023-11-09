package com.pshs.attendancesystem.websocket.handlers;


import com.pshs.attendancesystem.services.FrontEndWebSocketsCommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FrontEndWebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<WebSocketSession> sessions = new ArrayList<>();

    private final FrontEndWebSocketsCommunicationService communicationService;

    public FrontEndWebSocketHandler(FrontEndWebSocketsCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    /**
     * A method that is called after a connection is established in a WebSocket session.
     *
     * @param  session   the WebSocket session that was established
     * @throws Exception if there is an exception during the execution of the method
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
        logger.info("WebSocket Connection established: {}", session.getId());
        communicationService.registerFrontEndHandler(this);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    /**
     * A description of the afterConnectionClosed method.
     *
     * @param  session  the WebSocketSession object representing the current session
     * @param  status   the CloseStatus object representing the reason for the connection closure
     * @throws Exception if an exception occurs
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param  message  the message to be sent
     */
    public void sendMessageToAllClients(String message) {
        try {
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(message));
            }
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }
}

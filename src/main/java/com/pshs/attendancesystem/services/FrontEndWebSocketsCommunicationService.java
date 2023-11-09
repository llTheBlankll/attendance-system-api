package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.websocket.handlers.FrontEndWebSocketHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FrontEndWebSocketsCommunicationService {

    private final List<FrontEndWebSocketHandler> handlerList = new ArrayList<>();

    public void registerFrontEndHandler(FrontEndWebSocketHandler handler) {
        handlerList.add(handler);
    }

    public void sendMessageToFrontEndHandlers(String message) {
        for (FrontEndWebSocketHandler handler : handlerList) {
            handler.sendMessageToAllClients(message);
        }
    }
}

package com.pshs.attendancesystem.websocket;

import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.FrontEndWebSocketsCommunicationService;
import com.pshs.attendancesystem.websocket.handlers.FrontEndWebSocketHandler;
import com.pshs.attendancesystem.websocket.handlers.ScannerWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	private final RfidCredentialsRepository rfidCredentialsRepository;
	private final AttendanceService attendanceService;
	private final FrontEndWebSocketsCommunicationService communicationService;

	public WebSocketConfiguration(RfidCredentialsRepository rfidCredentialsRepository, AttendanceService attendanceService, FrontEndWebSocketsCommunicationService communicationService) {
		this.rfidCredentialsRepository = rfidCredentialsRepository;
		this.attendanceService = attendanceService;
		this.communicationService = communicationService;
	}

	/**
	 * Registers WebSocket handlers for the given WebSocketHandlerRegistry.
	 *
	 * @param handlerRegistry the WebSocketHandlerRegistry to register the handlers with. This allows for managing WebSocket connections and communication.
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry handlerRegistry) {
		handlerRegistry.addHandler(new ScannerWebSocketHandler(rfidCredentialsRepository, communicationService, attendanceService), "/websocket/scanner").setAllowedOrigins("*");
		handlerRegistry.addHandler(new FrontEndWebSocketHandler(communicationService), "/websocket/frontend").setAllowedOrigins("*");
	}
}

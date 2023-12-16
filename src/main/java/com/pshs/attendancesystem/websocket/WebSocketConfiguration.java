package com.pshs.attendancesystem.websocket;

import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.RfidService;
import com.pshs.attendancesystem.services.SectionService;
import com.pshs.attendancesystem.services.StudentService;
import com.pshs.attendancesystem.websocket.communication.FrontEndCommunicationService;
import com.pshs.attendancesystem.websocket.communication.SectionCommunicationService;
import com.pshs.attendancesystem.websocket.handlers.FrontEndWebSocketHandler;
import com.pshs.attendancesystem.websocket.handlers.ScannerWebSocketHandler;
import com.pshs.attendancesystem.websocket.handlers.SectionWebSocketHandler;
import com.pshs.attendancesystem.websocket.handlers.StudentWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	private final RfidService rfidService;
	private final AttendanceService attendanceService;
	private final StudentService studentService;
	private final SectionService sectionService;
	private final FrontEndCommunicationService frontEndCommunicationService;
	private final SectionCommunicationService sectionCommunicationService;

	public WebSocketConfiguration(RfidService rfidCredentialsRepository, AttendanceService attendanceService, StudentService studentService, SectionService sectionService, FrontEndCommunicationService frontEndCommunicationService, SectionCommunicationService sectionCommunicationService) {
		this.rfidService = rfidCredentialsRepository;
		this.attendanceService = attendanceService;
		this.studentService = studentService;
		this.sectionService = sectionService;
		this.frontEndCommunicationService = frontEndCommunicationService;
		this.sectionCommunicationService = sectionCommunicationService;
	}

	/**
	 * Registers WebSocket handlers for the given WebSocketHandlerRegistry.
	 *
	 * @param handlerRegistry the WebSocketHandlerRegistry to register the handlers with. This allows for managing WebSocket connections and communication.
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry handlerRegistry) {
		handlerRegistry.addHandler(new ScannerWebSocketHandler(rfidService, frontEndCommunicationService, attendanceService, sectionCommunicationService), "/websocket/scanner").setAllowedOrigins("*");
		handlerRegistry.addHandler(new FrontEndWebSocketHandler(frontEndCommunicationService), "/websocket/frontend").setAllowedOrigins("*");
		handlerRegistry.addHandler(new SectionWebSocketHandler(attendanceService, sectionService), "/websocket/section").setAllowedOrigins("*");
		handlerRegistry.addHandler(new StudentWebSocketHandler(studentService), "/websocket/student").setAllowedOrigins("*");
	}
}

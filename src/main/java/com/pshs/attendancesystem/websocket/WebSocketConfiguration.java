package com.pshs.attendancesystem.websocket;

import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.websocket.handlers.ScannerOutWebSocketHandler;
import com.pshs.attendancesystem.websocket.handlers.ScannerWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public WebSocketConfiguration(RfidCredentialsRepository rfidCredentialsRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.rfidCredentialsRepository = rfidCredentialsRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Registers WebSocket handlers for the given WebSocketHandlerRegistry.
     *
     * @param handlerRegistry the WebSocketHandlerRegistry to register the handlers with. This allows for managing WebSocket connections and communication.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry handlerRegistry) {
        handlerRegistry.addHandler(new ScannerWebSocketHandler(rfidCredentialsRepository, attendanceRepository, studentRepository), "/websocket/scanner/in").setAllowedOrigins("*");
        handlerRegistry.addHandler(new ScannerOutWebSocketHandler(rfidCredentialsRepository, attendanceRepository, studentRepository), "/websocket/scanner/out").setAllowedOrigins("*");
    }
}

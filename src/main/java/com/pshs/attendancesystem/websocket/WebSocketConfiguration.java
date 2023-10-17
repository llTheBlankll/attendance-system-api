package com.pshs.attendancesystem.websocket;

import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.ScanRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.websocket.Handlers.ScannerOutWebSocketHandler;
import com.pshs.attendancesystem.websocket.Handlers.ScannerWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final ScanRepository scanRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public WebSocketConfiguration(ScanRepository scanRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.scanRepository = scanRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry handlerRegistry) {
        handlerRegistry.addHandler(new ScannerWebSocketHandler(scanRepository, attendanceRepository, studentRepository), "/websocket/scanner/in").setAllowedOrigins("*");
        handlerRegistry.addHandler(new ScannerOutWebSocketHandler(scanRepository,attendanceRepository, studentRepository), "/websocket/scanner/out").setAllowedOrigins("*");
    }
}

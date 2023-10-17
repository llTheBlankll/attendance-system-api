package com.pshs.attendancesystem.websocket.Handlers;

import com.pshs.attendancesystem.entities.Scan;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.ScanRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;

public class ScannerOutWebSocketHandler extends TextWebSocketHandler {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ScanRepository scanRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerOutWebSocketHandler(ScanRepository scanRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.scanRepository = scanRepository;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String textMessage = message.getPayload();
        if (textMessage.isEmpty()) {
            return;
        }

        ManipulateAttendance manipulateAttendance = new ManipulateAttendance(this.attendanceRepository,this.studentRepository);
        Scan scan = this.scanRepository.findByHashedLrn(textMessage);

        if (scan != null && scan.getLrn() != null) {
            Student student = this.studentRepository.findStudentByLrn(scan.getLrn());

            if (student.getLrn() != null && manipulateAttendance.checkIfAlreadyOut(student)) {
                TextMessage alreadyOutMessage = new TextMessage("You've already left.");
                session.sendMessage(alreadyOutMessage);
                return;
            }

            manipulateAttendance.attendanceOut(student.getLrn());
            logger.info("Student " + student.getLrn() + " has left at " + LocalTime.now());
            session.sendMessage(new TextMessage("Student " + student.getLrn() + " has left at " + LocalTime.now()));
        }
    }
}

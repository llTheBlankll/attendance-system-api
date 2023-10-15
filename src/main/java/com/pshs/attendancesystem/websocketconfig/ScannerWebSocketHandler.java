package com.pshs.attendancesystem.websocketconfig;

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
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

public class ScannerWebSocketHandler extends TextWebSocketHandler {

    private final ScanRepository scanRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerWebSocketHandler(ScanRepository scanRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.scanRepository = scanRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ManipulateAttendance attendanceManipulate = new ManipulateAttendance(attendanceRepository, studentRepository);
        String hashedLrn = message.getPayload();
        // Check if empty
        if (hashedLrn.isEmpty()) {
            TextMessage emptyLrnMessage = new TextMessage("Empty LRN");
            session.sendMessage(emptyLrnMessage);
            return;
        }

        // Look for student by their LRN.
        Scan scan = this.scanRepository.findByHashedLrn(hashedLrn);

        // Check if student already arrived.
        Student student = this.studentRepository.findStudentByLrn(scan.getLrn());
        if (student.getLrn() != null && (attendanceManipulate.checkIfAlreadyArrived(student))) {
            TextMessage alreadyArrivedMessage = new TextMessage("You've already arrived.");
            session.sendMessage(alreadyArrivedMessage);
            return;
        }

        // Check if no matching lrn was found.
        if (scan.getLrn() != null) {
            // Change flag ceremony time if today is monday.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendar.getTime());
            boolean monday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
            LocalTime flagCeremonyTime;

            if (monday) {
                flagCeremonyTime = Time.valueOf("6:30:00").toLocalTime();
            } else {
                flagCeremonyTime = Time.valueOf("7:00:00").toLocalTime();
            }

            // Earliest time
            LocalTime earliestTimeToArrive = Time.valueOf("5:30:00").toLocalTime();

            // Get the current time
            Time currentTime = new Time(System.currentTimeMillis());
            LocalTime currentLocalTime = currentTime.toLocalTime();

            if (currentLocalTime.isBefore(flagCeremonyTime)) {
                TextMessage onTimeMessage = new TextMessage("You are on time");
                session.sendMessage(onTimeMessage);
            } else if (currentLocalTime.isAfter(flagCeremonyTime)) {
                TextMessage lateMessage = new TextMessage("You are late");
                session.sendMessage(lateMessage);
            } else if (currentLocalTime.isBefore(earliestTimeToArrive)){
                TextMessage earlyMessage = new TextMessage("You are early");
                session.sendMessage(earlyMessage);
            }

            // Now add attendance.
            attendanceManipulate.addAttendance(scan.getLrn());
        } else {
            TextMessage invalidLrnMessage = new TextMessage("Invalid LRN");
            session.sendMessage(invalidLrnMessage);
            logger.warn("Hashed LRN does not exist in the database.");
        }
    }
}

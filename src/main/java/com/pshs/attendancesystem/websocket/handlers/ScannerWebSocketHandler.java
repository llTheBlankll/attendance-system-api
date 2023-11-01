package com.pshs.attendancesystem.websocket.handlers;

import com.pshs.attendancesystem.AttendanceSystemConfiguration;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
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

    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerWebSocketHandler(RfidCredentialsRepository rfidCredentialsRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.rfidCredentialsRepository = rfidCredentialsRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    private void sendErrorMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        session.sendMessage(textMessage);
    }

    /**
     * Handles a text message in the WebSocket session.
     * This method is responsible for handling incoming text messages in the WebSocket session. It performs the following steps:
     * 1. Creates an instance of the ManipulateAttendance class, passing in the attendanceRepository and studentRepository as arguments.
     * 2. Retrieves the payload of the text message using message.getPayload().
     * 3. Checks if the payload is empty. If so, it sends a text message back to the client with the content "Empty LRN" and returns.
     * 4. Looks for a student scan in the scanRepository based on the hashed LRN obtained from the text message.
     * 5. Checks if the student has already arrived by retrieving the student from the studentRepository based on the LRN obtained from the scan object. If the student has already arrived, it sends a text message back to the client with the content "You've already arrived" and returns.
     * 6. Checks if no matching LRN was found. If a matching LRN is found, it follows these steps:
     * a. Determines the flag ceremony time based on the current day. If it is Monday, the flag ceremony time is set to 6:30 AM. Otherwise, it is set to 7:00 AM.
     * b. Sets the earliest time to arrive as 5:30 AM.
     * c. Retrieves the current time and stores it as currentTime.
     * d. Compares the current time with the flag ceremony time and sends the appropriate text message back to the client based on the result.
     * e. Adds the attendance by calling the addAttendance method of the attendanceManipulate object.
     * 7. If no matching LRN is found, it sends a text message back to the client with the content "Invalid LRN", logs a warning message, and returns.
     *
     * @param session the WebSocket session in which the message was received
     * @param message the text message received
     * @throws IOException if an I/O error occurs while sending a response to the client
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ManipulateAttendance attendanceManipulate = new ManipulateAttendance(attendanceRepository, studentRepository);
        String hashedLrn = message.getPayload();
        // Check if empty
        if (hashedLrn.isEmpty()) {
            session.sendMessage(new TextMessage("Empty LRN"));
            return;
        }


        // Look for student by their LRN.
        RfidCredentials rfidCredentials;
        if (!this.rfidCredentialsRepository.existsByHashedLrn(hashedLrn)) {
            sendErrorMessage(session, new TextMessage("Invalid LRN"));
            logger.warn("Hashed LRN does not exist in the database.");
            return;
        }

        rfidCredentials = this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);

        // Check if student already arrived.
        Student student = this.studentRepository.findStudentByLrn(rfidCredentials.getLrn());
        if (student.getLrn() != null && (attendanceManipulate.checkIfAlreadyArrived(student))) {
            session.sendMessage(new TextMessage("You've already arrived."));
            return;
        }

        // Check if no matching lrn was found.
        if (rfidCredentials.getLrn() != null) {
            // Change flag ceremony time if today is monday.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendar.getTime());
            boolean monday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
            LocalTime lateTime;

            if (monday) {
                lateTime = AttendanceSystemConfiguration.Attendance.flagCeremonyTime;
            } else {
                lateTime = AttendanceSystemConfiguration.Attendance.lateTimeArrival;
            }

            // Earliest time
            LocalTime onTimeArrival = AttendanceSystemConfiguration.Attendance.onTimeArrival;

            // Get the current time
            Time currentTime = new Time(System.currentTimeMillis());
            LocalTime currentLocalTime = currentTime.toLocalTime();

            if (currentLocalTime.isBefore(lateTime)) {
                TextMessage onTimeMessage = new TextMessage(AttendanceMessages.ATTENDANCE_ONTIME);
                session.sendMessage(onTimeMessage);
            } else if (currentLocalTime.isAfter(lateTime)) {
                TextMessage lateMessage = new TextMessage(AttendanceMessages.ATTENDANCE_LATE);
                session.sendMessage(lateMessage);
            } else if (currentLocalTime.isBefore(onTimeArrival)) {
                TextMessage earlyMessage = new TextMessage(AttendanceMessages.ATTENDANCE_EARLY);
                session.sendMessage(earlyMessage);
            }

            // Now add attendance.
            attendanceManipulate.addAttendance(rfidCredentials.getLrn());
        } else {
            // Send a warning message, because there might be an error in scanner.
            TextMessage invalidLrnMessage = new TextMessage(StudentMessages.STUDENT_INVALID_LRN);
            session.sendMessage(invalidLrnMessage);
            logger.warn(RfidMessages.HASHED_LRN_NOT_FOUND);
        }
    }
}

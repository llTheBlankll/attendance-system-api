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

    /**
     * Handles a text message in the WebSocket session.
     *
     * This method is responsible for handling incoming text messages in the WebSocket session. It performs the following steps:
     *
     * 1. Creates an instance of the ManipulateAttendance class, passing in the attendanceRepository and studentRepository as arguments.
     * 2. Retrieves the payload of the text message using message.getPayload().
     * 3. Checks if the payload is empty. If so, it sends a text message back to the client with the content "Empty LRN" and returns.
     * 4. Looks for a student scan in the scanRepository based on the hashed LRN obtained from the text message.
     * 5. Checks if the student has already arrived by retrieving the student from the studentRepository based on the LRN obtained from the scan object. If the student has already arrived, it sends a text message back to the client with the content "You've already arrived" and returns.
     * 6. Checks if no matching LRN was found. If a matching LRN is found, it follows these steps:
     *     a. Determines the flag ceremony time based on the current day. If it is Monday, the flag ceremony time is set to 6:30 AM. Otherwise, it is set to 7:00 AM.
     *     b. Sets the earliest time to arrive as 5:30 AM.
     *     c. Retrieves the current time and stores it as currentTime.
     *     d. Compares the current time with the flag ceremony time and sends the appropriate text message back to the client based on the result.
     *     e. Adds the attendance by calling the addAttendance method of the attendanceManipulate object.
     * 7. If no matching LRN is found, it sends a text message back to the client with the content "Invalid LRN", logs a warning message, and returns.
     *
     * @param session  the WebSocket session in which the message was received
     * @param message  the text message received
     * @throws IOException  if an I/O error occurs while sending a response to the client
     */
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
            // Send a warning message, because there might be an error in scanner.
            TextMessage invalidLrnMessage = new TextMessage("Invalid LRN");
            session.sendMessage(invalidLrnMessage);
            logger.warn("Hashed LRN does not exist in the database.");
        }
    }
}

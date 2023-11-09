package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.AttendanceSystemConfiguration;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.WebSocketData;
import com.pshs.attendancesystem.entities.WebSocketResponse;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.FrontEndWebSocketsCommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScannerWebSocketHandler extends TextWebSocketHandler {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final FrontEndWebSocketsCommunicationService communicationService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<WebSocketSession> frontEndSessions = new ArrayList<>();

    public ScannerWebSocketHandler(RfidCredentialsRepository rfidCredentialsRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository, FrontEndWebSocketsCommunicationService communicationService) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.rfidCredentialsRepository = rfidCredentialsRepository;
        this.communicationService = communicationService;
    }

    private void sendErrorMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        session.sendMessage(textMessage);
    }

    /**
     * Overrides the handleTextMessage method from the WebSocketHandlerAdapter class.
     * Handles a WebSocket text message received.
     * This function processes the received text message and performs various operations based on its content.
     * It follows the following steps:
     * 1. Retrieves the payload of the text message using message.getPayload().
     * 2. If the payload is empty, the function returns and does nothing.
     * 3. Creates an instance of the ManipulateAttendance class, passing in the attendanceRepository and studentRepository as arguments.
     * 4. Retrieves a Scan object from the scanRepository based on the hashed LRN (Learning Reference Number) obtained from the text message.
     * 5. If a valid Scan object is found, and it has a valid LRN, the function proceeds with further processing.
     * 6. Retrieves a Student object from the studentRepository based on the LRN obtained from the Scan object.
     * 7. If the Student object has a valid LRN and the attendance for the student has already been marked as "out", it sends a text message back to the client with the content "You've already left," and the function returns.
     * 8. If the above condition is not met, the function marks the attendance for the student as "out" using the manipulateAttendance.attendanceOut() method.
     * 9. It logs a message indicating that the student has left, and sends a text message back to the client with the same information.
     *
     * @param session the WebSocket session
     * @param message the received text message
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String textMessage = message.getPayload();
        if (textMessage.isEmpty()) {
            return;
        }

        String hashedLrn;
        RfidCredentials rfidCredentials;
        WebSocketResponse response = new WebSocketResponse();
        ManipulateAttendance attendanceManipulate = new ManipulateAttendance(attendanceRepository, studentRepository);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        try {
            WebSocketData webSocketData = mapper.readValue(textMessage, WebSocketData.class);
            hashedLrn = webSocketData.getHashedLrn();

            rfidCredentials = this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);

            // Check if student already arrived.
            Student student = this.studentRepository.findStudentByLrn(rfidCredentials.getLrn());

            if (webSocketData.getMode().equals("in")) {
                if (!this.rfidCredentialsRepository.existsByHashedLrn(hashedLrn)) {
                    sendErrorMessage(session, new TextMessage("Hashed LRN does not exist in the database. Mode IN"));
                    logger.warn("Hashed LRN does not exist in the database.");
                    return;
                }

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
                    Status attendanceStatus = attendanceManipulate.createAttendance(rfidCredentials.getLrn());
                    response.setMessage("You're " + attendanceStatus);
                    response.setStatus(attendanceStatus);
                    response.setStudentLrn(rfidCredentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStudent(student);

                    session.sendMessage(new TextMessage(
                        mapper.writeValueAsBytes(response)
                    ));

                    communicationService.sendMessageToFrontEndHandlers(
                        mapper.writeValueAsString(response)
                    );
                } else {
                    // Send a warning message, because there might be an error in scanner.
                    TextMessage invalidLrnMessage = new TextMessage(StudentMessages.STUDENT_INVALID_LRN);
                    session.sendMessage(invalidLrnMessage);
                    logger.warn(RfidMessages.HASHED_LRN_NOT_FOUND);
                }
            } else if (webSocketData.getMode().equals("out")) {
                rfidCredentials = this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);

                if (rfidCredentials != null && rfidCredentials.getLrn() != null) {

                    if (attendanceManipulate.checkIfAlreadyOut(rfidCredentials.getLrn())) {
                        TextMessage alreadyOutMessage = new TextMessage("You've already scanned out");
                        session.sendMessage(alreadyOutMessage);
                        return;
                    }

                    if (!attendanceManipulate.attendanceOut(rfidCredentials.getLrn())) {
                        session.sendMessage(new TextMessage("Failed to mark attendance as out. Mode OUT"));
                    }
                    logger.info("Student {} has left at {}", rfidCredentials.getLrn(), Time.valueOf(LocalTime.now()));

                    // Get Attendance
                    response.setMessage("Bye Bye, ingat");
                    response.setStudentLrn(rfidCredentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStatus(Status.OUT);
                    response.setStudent(student);

                    // Send response
                    session.sendMessage(new TextMessage(
                        mapper.writeValueAsBytes(response))
                    );

                    communicationService.sendMessageToFrontEndHandlers(
                        mapper.writeValueAsString(response)
                    );
                }
            }
        } catch (JsonParseException e) {
            logger.error("Invalid JSON");
        }
    }
}

package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.*;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.FrontEndWebSocketsCommunicationService;
import com.pshs.attendancesystem.threading.SMSThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

@Component
public class ScannerWebSocketHandler extends TextWebSocketHandler {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final FrontEndWebSocketsCommunicationService communicationService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerWebSocketHandler(RfidCredentialsRepository rfidCredentialsRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository, FrontEndWebSocketsCommunicationService communicationService) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.rfidCredentialsRepository = rfidCredentialsRepository;
        this.communicationService = communicationService;
    }


    private void sendErrorMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        session.sendMessage(textMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    /**
     * Handles a text message received from a WebSocket session.
     * This method is responsible for processing text messages received from a WebSocket session. It takes the WebSocket
     * session and the text message as parameters. If any I/O error occurs during the processing of the message, an
     * IOException is thrown.
     * The function starts by extracting the payload of the message using the getPayload() method of the message object.
     * If the payload is empty, the function returns without performing any further processing.
     * Next, the function initializes variables such as hashedLrn, rfidCredentials, response, and attendanceManipulate.
     * An instance of the ObjectMapper class is created and configured to handle the deserialization of the text message.
     * The text message is deserialized into a WebSocketData object using the ObjectMapper. If the deserialization is
     * successful, the hashedLrn is obtained from the WebSocketData object.
     * The function retrieves the rfidCredentials from the rfidCredentialsRepository based on the hashedLrn.
     * If the mode of the WebSocketData object is "in", the function performs a series of checks and operations related
     * to student attendance. It checks if the hashedLrn exists in the database and if the student has already arrived.
     * It also adjusts the flag ceremony time based on the current day and sends appropriate messages to the WebSocket
     * session.
     * If the mode of the WebSocketData object is "out", the function performs checks and operations related to marking
     * the student as "out". It checks if the student has already scanned out and marks the attendance as "out" if not.
     * It also sends a goodbye message to the WebSocket session.
     * Please note that this is just a summary of the function's logic. The actual implementation may involve additional
     * details and complexities.
     *
     * @param session  the WebSocket session representing the connection
     * @param message  the text message received from the session
     * @throws IOException  if an I/O error occurs while processing the message
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String textMessage = message.getPayload();
        if (textMessage.isEmpty()) {
            return;
        }

        String hashedLrn;
        Optional<RfidCredentials> rfidCredentials;
        WebSocketResponse response = new WebSocketResponse();
        ManipulateAttendance attendanceManipulate = new ManipulateAttendance(attendanceRepository, studentRepository);
        Student student;
        RfidCredentials credentials;

        // Get the current time
        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        try {
            WebSocketData webSocketData = mapper.readValue(textMessage, WebSocketData.class);
            hashedLrn = webSocketData.getHashedLrn();

            rfidCredentials = this.rfidCredentialsRepository.findByHashedLrn(hashedLrn);
            if (rfidCredentials.isPresent()) {
                student = rfidCredentials.get().getStudent();
                credentials = rfidCredentials.get();
            } else {
                response.setMessage("Invalid");
                session.sendMessage(new TextMessage(
                        mapper.writeValueAsString(response)
                ));
                return;
            }

            // Check if student already arrived.

            if (webSocketData.getMode().equals("in")) {
                if (!this.rfidCredentialsRepository.existsByHashedLrn(hashedLrn)) {
                    sendErrorMessage(session, new TextMessage("Hashed LRN does not exist in the database. Mode IN"));
                    logger.warn("Hashed LRN does not exist in the database.");
                    return;
                }

                if (student.getLrn() != null && (attendanceManipulate.checkIfAlreadyArrived(student))) {
                    Status attendanceStatus = attendanceManipulate.getAttendanceStatusToday(student.getLrn());
                    response.setMessage("Already arrived!");
                    response.setStudentLrn(credentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStudent(student);
                    response.setStatus(attendanceStatus);
                    session.sendMessage(new TextMessage(
                        mapper.writeValueAsString(response)
                    ));
                    return;
                }

                // Check if no matching lrn was found.
                if (credentials.getLrn() != null) {
                    // Change flag ceremony time if today is monday.
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(calendar.getTime());

                    // Now add attendance.
                    Status attendanceStatus = attendanceManipulate.createAttendance(credentials.getLrn());
                    response.setMessage("You are " + attendanceStatus);
                    response.setStatus(attendanceStatus);
                    response.setStudentLrn(credentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStudent(student);
                    response.setStatus(attendanceStatus);

                    String parentMessage = AttendanceMessages.onTimeAttendanceMessage(String.format("%s %s. %s", student.getFirstName(), student.getMiddleName(), student.getLastName()), currentLocalTime.toString());
                    guardianSet(student, parentMessage);

                    session.sendMessage(new TextMessage(
                        mapper.writeValueAsBytes(response)
                    ));

                    communicationService.sendMessageToAllFrontEnd(
                        mapper.writeValueAsString(response)
                    );
                } else {
                    // Send a warning message, because there might be an error in scanner.
                    TextMessage invalidLrnMessage = new TextMessage(StudentMessages.STUDENT_INVALID_LRN);
                    session.sendMessage(invalidLrnMessage);
                    logger.warn(RfidMessages.HASHED_LRN_NOT_FOUND);
                }
            } else if (webSocketData.getMode().equals("out") && credentials.getLrn() != null) {
                    if (attendanceManipulate.checkIfAlreadyOut(credentials.getLrn())) {
                        Status attendanceStatus = attendanceManipulate.getAttendanceStatusToday(credentials.getLrn());
                        response.setMessage("Already scanned!");
                        response.setStatus(attendanceStatus);
                        response.setStudentLrn(credentials.getLrn());
                        response.setTime(Time.valueOf(LocalTime.now()));
                        response.setStudent(student);
                        session.sendMessage(new TextMessage(
                            mapper.writeValueAsString(response)
                        ));
                        return;
                    }

                    if (!attendanceManipulate.attendanceOut(credentials.getLrn())) {
                        response.setMessage("You need to scan first.");
                        session.sendMessage(new TextMessage(
                            mapper.writeValueAsString(response)
                        ));
                        return;
                    }
                    logger.info("Student {} has left at {}", credentials.getLrn(), Time.valueOf(LocalTime.now()));

                    // Get Attendance
                    response.setMessage("Bye Bye :)");
                    response.setStudentLrn(credentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStatus(Status.OUT);
                    response.setStudent(student);

                    // Send response
                    session.sendMessage(new TextMessage(
                        mapper.writeValueAsBytes(response))
                    );

                    communicationService.sendMessageToAllFrontEnd(
                        mapper.writeValueAsString(response)
                    );

                String parentMessage = AttendanceMessages.StudentAttendedMessage(String.format("%s %s. %s", student.getFirstName(), student.getMiddleName(), student.getLastName()), currentLocalTime.toString());

                guardianSet(student, parentMessage);
            }
        } catch (JsonParseException e) {
            logger.error("Invalid JSON");
        }
    }

    private void guardianSet(Student student, String parentMessage) {
        Set<Guardian> guardianSet = student.getGuardian();
        for (Guardian guardian : guardianSet) {
            if (guardian.getContactNumber().equals("null")) {
                logger.error("No contact number found");
                return;
            }

            SMSThread smsThread = new SMSThread(parentMessage, guardian);
            smsThread.start();
        }
    }
}

package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.*;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.RfidCredentialsRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.FrontEndWebSocketsCommunicationService;
import com.pshs.attendancesystem.threading.SMSThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

@Component
public class ScannerWebSocketHandler extends TextWebSocketHandler {
    private final AttendanceService attendanceService;
    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final FrontEndWebSocketsCommunicationService communicationService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerWebSocketHandler(RfidCredentialsRepository rfidCredentialsRepository, FrontEndWebSocketsCommunicationService communicationService, AttendanceService attendanceService) {
        this.rfidCredentialsRepository = rfidCredentialsRepository;
        this.communicationService = communicationService;
        this.attendanceService = attendanceService;
    }


    private void sendErrorMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
        session.sendMessage(textMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String textMessage = message.getPayload();
        if (textMessage.isEmpty()) {
            return;
        }

        String hashedLrn;
        Optional<RfidCredentials> rfidCredentials;
        WebSocketResponse response = new WebSocketResponse();
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

                if (student.getLrn() != null && (attendanceService.checkIfAlreadyArrived(student))) {
                    Status attendanceStatus = attendanceService.getAttendanceStatusToday(student.getLrn());
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
                    Status attendanceStatus = attendanceService.createAttendance(credentials.getLrn());
                    response.setMessage("You are " + attendanceStatus);
                    response.setStatus(attendanceStatus);
                    response.setStudentLrn(credentials.getLrn());
                    response.setTime(Time.valueOf(LocalTime.now()));
                    response.setStudent(student);
                    response.setStatus(attendanceStatus);

                    if (attendanceStatus == Status.LATE) {
                        String parentMessage = AttendanceMessages.onLateAttendanceMessage(getFullName(student), currentLocalTime.toString());
                        informGuardian(student, parentMessage);
                    } else if (attendanceStatus == Status.ONTIME) {
                        String parentMessage = AttendanceMessages.onTimeAttendanceMessage(getFullName(student), currentLocalTime.toString());
                        informGuardian(student, parentMessage);
                    }

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
                if (attendanceService.checkIfAlreadyOut(credentials.getLrn())) {
                    Status attendanceStatus = attendanceService.getAttendanceStatusToday(credentials.getLrn());
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

                if (!attendanceService.attendanceOut(credentials.getLrn())) {
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

                String parentMessage = AttendanceMessages.studentOutOfFacility(getFullName(student), currentLocalTime.toString());
                informGuardian(student, parentMessage);
            }
        } catch (JsonParseException e) {
            logger.error("Invalid JSON");
        } catch (SocketException exception) {
            logger.error("Socket error: {}", exception.getMessage());
        }
    }

    private void informGuardian(Student student, String parentMessage) {
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

    private String getFullName(Student student) {
        return String.format("%s %s %s", student.getLastName(), student.getFirstName(), student.getMiddleName());
    }
}

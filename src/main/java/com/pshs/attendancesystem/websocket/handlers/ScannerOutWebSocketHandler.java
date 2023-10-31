package com.pshs.attendancesystem.websocket.handlers;

import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
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

public class ScannerOutWebSocketHandler extends TextWebSocketHandler {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final RfidCredentialsRepository rfidCredentialsRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScannerOutWebSocketHandler(RfidCredentialsRepository rfidCredentialsRepository, AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.rfidCredentialsRepository = rfidCredentialsRepository;
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

        ManipulateAttendance manipulateAttendance = new ManipulateAttendance(this.attendanceRepository, this.studentRepository);
        RfidCredentials rfidCredentials = this.rfidCredentialsRepository.findByHashedLrn(textMessage);

        if (rfidCredentials != null && rfidCredentials.getLrn() != null) {
            Student student = this.studentRepository.findStudentByLrn(rfidCredentials.getLrn());

            if (student.getLrn() != null && manipulateAttendance.checkIfAlreadyOut(student)) {
                TextMessage alreadyOutMessage = new TextMessage("You've already left.");
                session.sendMessage(alreadyOutMessage);
                return;
            }

            if (!manipulateAttendance.attendanceOut(student.getLrn())) {
                session.sendMessage(new TextMessage("Invalid LRN"));
            }
            logger.info("Student {} has left at {}", student.getLrn(), Time.valueOf(LocalTime.now()));
            session.sendMessage(new TextMessage("Student " + student.getLrn() + " has left at " + Time.valueOf(LocalTime.now())));
        }
    }
}

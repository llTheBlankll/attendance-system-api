package com.pshs.attendancesystem.websocketconfig;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Scan;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.ScanRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

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

    public void addAttendance(@PathVariable Long student_lrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(student_lrn)) {
            logger.info("Student LRN does not exist");
        }

        LocalTime flagCeremonyTime = Time.valueOf("7:00:00").toLocalTime();
        LocalTime earliestTimeToArrive = Time.valueOf("4:00:00").toLocalTime();
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        Optional<Student> student = this.studentRepository.findById(student_lrn);
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (currentLocalTime.isBefore(flagCeremonyTime) && currentLocalTime.isAfter(earliestTimeToArrive)) {
                attendance.setAttendance_status(Enums.status.ONTIME);
            } else if (currentLocalTime.isAfter(flagCeremonyTime)) {
                attendance.setAttendance_status(Enums.status.LATE);
            } else {
                // EARLY
            }

            attendance.setDate(date);
            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setId(attendanceRepository.getNextSeriesId());
            this.attendanceRepository.save(attendance);
            logger.info("The student is " + attendance.getAttendance_status());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String hashedLrn = message.getPayload();
        // Check if empty
        if (hashedLrn.isEmpty()) {
            TextMessage emptyLrnMessage = new TextMessage("Empty LRN");
            session.sendMessage(emptyLrnMessage);
            return;
        }

        Scan scan = this.scanRepository.findByHashedLrn(hashedLrn);

        // Check if no matching lrn was found.
        if (scan != null) {
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
            this.addAttendance(scan.getLrn());
        } else {
            TextMessage invalidLrnMessage = new TextMessage("Invalid LRN");
            session.sendMessage(invalidLrnMessage);
            logger.warn("Hashed LRN does not exist in the database.");
        }
    }
}

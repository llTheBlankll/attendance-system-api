package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class ManipulateAttendance {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ManipulateAttendance(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public boolean checkIfAlreadyArrived(Student student) {

        // Iterate each attendance and get the attendance with the current date time,
        // If a row exists, return false because the student has already arrived.
        for (Attendance currentAttendance : student.getAttendances()) {
            if (currentAttendance.getDate().equals(LocalDate.now())) {
                logger.info(String.format("Student %s already arrived", student.getLrn()));
                return true;
            }
        }

        return false;
    }

    public boolean addAttendance(Long studentLrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            logger.info("Student LRN does not exist");
            return false;
        }

        LocalTime flagCeremonyTime = Time.valueOf("7:00:00").toLocalTime();
        LocalTime earliestTimeToArrive = Time.valueOf("4:00:00").toLocalTime();

        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        Optional<Student> student = this.studentRepository.findById(studentLrn);
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (currentLocalTime.isBefore(flagCeremonyTime) && currentLocalTime.isAfter(earliestTimeToArrive)) {
                attendance.setAttendanceStatus(Enums.status.ONTIME);
            } else if (currentLocalTime.isAfter(flagCeremonyTime)) {
                attendance.setAttendanceStatus(Enums.status.LATE);
            } else {
                // EARLY
            }

            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setDate(LocalDate.now());
            this.attendanceRepository.save(attendance);
            logger.info(String.format("The student %s is %s, Time arrived: %s", student.get().getLrn(), attendance.getAttendanceStatus(), currentTime));
            return true;
        }

        return false;
    }

    public Iterable<Attendance> getAllAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Enums.status status) {
        return attendanceRepository.findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(startDate, endDate, status);
    }
}

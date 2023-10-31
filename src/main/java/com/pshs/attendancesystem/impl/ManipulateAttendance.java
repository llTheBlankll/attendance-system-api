package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.AttendanceSystemConfiguration;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class ManipulateAttendance {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ManipulateAttendance(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Checks if the student has already arrived by iterating through their attendances and comparing the date.
     *
     * @param student the student object to check
     * @return true if the student has already arrived, false otherwise
     */
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

    /**
     * Checks if today is Monday.
     *
     * @return true if today is Monday, false otherwise
     */
    private boolean isTodayMonday() {
        return LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY);
    }

    /**
     * Checks if the student has already left.
     *
     * @param student the student object
     * @return true if the student has already left, false otherwise
     */
    public boolean checkIfAlreadyOut(Student student) {
        // Iterate eachAttendance and get theAttendance with the current date time,
        // If a row exists, return false because the student has already left.
        for (Attendance currentAttendance : student.getAttendances()) {
            if (currentAttendance.getDate().equals(LocalDate.now())) {
                // If the student has already left, return true
                if (currentAttendance.getTimeOut() != null) {
                    logger.info("Student {} already left", student.getLrn());
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    /**
     * Adds attendance for a student based on their LRN.
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @return true if the attendance is successfully added, false otherwise
     */
    public boolean addAttendance(Long studentLrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            logger.info("Student LRN does not exist");
            return false;
        }

        LocalTime lateArrivalTime;
        LocalTime onTimeArrival = AttendanceSystemConfiguration.Attendance.onTimeArrival;

        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        // Get Student Data from the database.
        Optional<Student> student = this.studentRepository.findById(studentLrn);

        // Flag Ceremony Time
        if (isTodayMonday()) {
            lateArrivalTime = AttendanceSystemConfiguration.Attendance.flagCeremonyTime;
        } else {
            lateArrivalTime = AttendanceSystemConfiguration.Attendance.lateTimeArrival;
        }

        // Check if the data is valid.
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (currentLocalTime.isBefore(lateArrivalTime) && currentLocalTime.isAfter(onTimeArrival)) {
                attendance.setAttendanceStatus(Status.ONTIME);
            } else if (currentLocalTime.isAfter(lateArrivalTime)) {
                attendance.setAttendanceStatus(Status.LATE);
            } // ADD CODE HERE FOR EARLY ARRIVAL.

            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setDate(LocalDate.now());
            this.attendanceRepository.save(attendance);
            logger.info("The student {} is {}, Time arrived: {}", student.get().getLrn(), attendance.getAttendanceStatus(), currentTime);
            return true;
        }

        return false;
    }

    /**
     * Checks the attendance status of a student and marks them as "out" if they are currently "in".
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @return true if the student's attendance was successfully marked as "out", false otherwise
     */
    public boolean attendanceOut(Long studentLrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
            return false;
        }

        Optional<Student> student = this.studentRepository.findById(studentLrn);
        if (student.isPresent()) {
            Student studentData = student.get();
            for (Attendance attendance : studentData.getAttendances()) {
                if (attendance.getDate().equals(LocalDate.now()) && attendance.getTimeOut() == null) {
                    this.attendanceRepository.studentAttendanceOut(LocalTime.now(), attendance.getId());
                    logger.info("Student {} has left at {}", studentData.getLrn(), LocalTime.now());
                    return true;
                }
            }
            return true;
        }

        return false;
    }

    public Iterable<Attendance> getAllAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(startDate, endDate, status);
    }

    public long getAllCountOfAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.countByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(startDate, endDate, status);
    }

    public Iterable<Attendance> getStudentAttendanceBetweenDateWithAttendanceStatus(long studentLrn
            , LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(studentLrn, startDate, endDate, status);
    }

    public Iterable<Attendance> getStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(studentLrn, startDate, endDate);
    }

    public long getAllCountOfStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(studentLrn, startDate, endDate, status);
    }
}

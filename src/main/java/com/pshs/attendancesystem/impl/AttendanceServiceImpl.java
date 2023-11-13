package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.AttendanceSystemConfiguration;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, StudentService studentService) {
        this.attendanceRepository = attendanceRepository;
        this.studentService = studentService;
    }

    /**
     * Checks if today is Monday.
     *
     * @return true if today is Monday, false otherwise
     */
    private boolean isTodayMonday() {
        return LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY);
    }

    @Override
    public Iterable<Attendance> getAllAttendances() {
        return this.attendanceRepository.findAll();
    }

    @Override
    public String createAttendance(Long studentLrn) {
        // Check for the existence of Student LRN
        if (!this.studentService.studentExistsByLrn(studentLrn)) {
            logger.info("Student LRN does not exist");
            return StudentMessages.STUDENT_NOT_FOUND;
        }

        LocalTime lateArrivalTime;
        LocalTime onTimeArrival = AttendanceSystemConfiguration.Attendance.onTimeArrival;

        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        // Get Student Data from the database.
        Student student = this.studentService.getStudentById(studentLrn);

        // Flag Ceremony Time
        if (isTodayMonday()) {
            lateArrivalTime = AttendanceSystemConfiguration.Attendance.flagCeremonyTime;
        } else {
            lateArrivalTime = AttendanceSystemConfiguration.Attendance.lateTimeArrival;
        }

        // Check if the data is valid.
        if (student.getLrn() != null) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student);

            if (currentLocalTime.isBefore(lateArrivalTime) && currentLocalTime.isAfter(onTimeArrival)) {
                attendance.setAttendanceStatus(Status.ONTIME);
            } else if (currentLocalTime.isAfter(lateArrivalTime)) {
                attendance.setAttendanceStatus(Status.LATE);
            } // ADD CODE HERE FOR EARLY ARRIVAL.

            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setDate(LocalDate.now());
            this.attendanceRepository.save(attendance);
            logger.info("The student {} is {}, Time arrived: {}", student.getLrn(), attendance.getAttendanceStatus(), currentTime);
            return (attendance.getAttendanceStatus() == Status.LATE) ? AttendanceMessages.ATTENDANCE_LATE : AttendanceMessages.ATTENDANCE_ONTIME;
        }

        return StudentMessages.STUDENT_NOT_FOUND;
    }

    @Override
    public String deleteAttendance(Integer attendanceId) {
        Optional<Attendance> attendanceOptional = this.attendanceRepository.findById(attendanceId);
        if (attendanceOptional.isPresent()) {
            this.attendanceRepository.deleteById(attendanceId);
            return AttendanceMessages.ATTENDANCE_DELETED;
        }

        return AttendanceMessages.ATTENDANCE_NOT_FOUND;
    }

    @Override
    public String updateAttendance(Attendance attendance) {
        if (this.attendanceRepository.existsById(attendance.getId())) {
            this.attendanceRepository.save(attendance);
            return AttendanceMessages.ATTENDANCE_UPDATED;
        }

        return AttendanceMessages.ATTENDANCE_NOT_FOUND;
    }

    @Override
    public boolean existsByAttendanceId(Integer attendanceId) {
        return this.attendanceRepository.existsById(attendanceId);
    }
}

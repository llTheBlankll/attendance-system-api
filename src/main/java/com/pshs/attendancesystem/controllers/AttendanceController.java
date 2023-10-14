package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    @PutMapping("/add/{studentLrn}")
    public String addAttendance(@PathVariable Long studentLrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            return "Student LRN does not exist";
        }
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

        LocalTime earliestTimeToArrive = Time.valueOf("5:30:00").toLocalTime();
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        Optional<Student> student = this.studentRepository.findById(studentLrn);
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (currentLocalTime.isBefore(flagCeremonyTime) && currentLocalTime.isAfter(earliestTimeToArrive)) {
                attendance.setAttendance_status(Enums.status.ONTIME);
            } else if (currentLocalTime.isAfter(flagCeremonyTime)) {
                attendance.setAttendance_status(Enums.status.LATE);
            } else if (currentLocalTime.isBefore(earliestTimeToArrive)) {
                // EARLY
            }

            attendance.setDate(date);
            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setId(attendanceRepository.getNextSeriesId());
            this.attendanceRepository.save(attendance);
            return "The student is " + attendance.getAttendance_status();
        }

        return "Student does not exist";
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteAttendance(@PathVariable Integer id) {
        if (!this.attendanceRepository.existsById(id)) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.deleteById(id);
        return "Attendance was deleted";
    }

    @PostMapping ("/update")
    public String updateAttendance(@RequestBody Attendance attendance) {
        if (!this.attendanceRepository.existsById(attendance.getId())) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.save(attendance);
        return "Attendance was updated";
    }
}
